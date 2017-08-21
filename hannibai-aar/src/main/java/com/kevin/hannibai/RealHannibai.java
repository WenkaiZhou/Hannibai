/*
 * Copyright (c) 2017 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.hannibai;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.kevin.hannibai.Utils.checkNotNull;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

final class RealHannibai {

    private static final String TAG = "RealHannibai";

    private Context mContext;
    private Converter.Factory converterFactory;

    private RealHannibai() {
    }

    public static RealHannibai getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RealHannibai INSTANCE = new RealHannibai();
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    final public <T> T create(final Class<T> preference) {
        Utils.validateHandleInterface(preference);
        try {
            return (T) Class.forName(preference.getName() + "Impl")
                    .getMethod("getInstance")
                    .invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("出错了");
        }
    }

    final public <T> T create(final Class<T> preference, String id) {
        Utils.validateHandleInterface(preference);
        try {
            return (T) Class.forName(preference.getName() + "Impl")
                    .getConstructor(String.class)
                    .newInstance(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("出错了");
        }
    }

    final Context getContext() {
        return checkNotNull(mContext, "context == null");
    }

    final SharedPreferences getSharedPreferences(String name, String id) {
        return getContext().getSharedPreferences(name + id, Context.MODE_PRIVATE);
    }

    final <T> T get(String name, String id, String key, T defValue) {
        if (Hannibai.debug) Log.d(TAG, String.format("Retrieve the %s from the preferences.", key));
        String value = getSharedPreferences(name, id).getString(key, null);
        if (value == null || value.length() == 0) {
            if (Hannibai.debug)
                Log.d(TAG, String.format("Value is empty, returns the default value ( %s ).", defValue));
            return defValue;
        } else {
            ParameterizedType type = type(BaseModel.class, defValue.getClass());
            BaseModel<T> model = (BaseModel<T>) getConverterFactory().toType(type).convert(Utils.endecode(value));
            if (Hannibai.debug)
                Log.d(TAG, String.format("Value is %s, create at %s, update at %s.", model.data, model.createTime, model.updateTime));
            return model.data;
        }
    }

    final <T> void set(String name, String id, String key, T newValue) {
        if (Hannibai.debug) Log.d(TAG, String.format("Set the %s value to the preferences.", key));
        BaseModel<T> model;
        ParameterizedType type = type(BaseModel.class, newValue.getClass());
        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
        String value = sharedPreferences.getString(key, null);
        if (value != null && value.length() != 0) {
            model = (BaseModel<T>) getConverterFactory().toType(type).convert(value);
            model.update(newValue);
        } else {
            model = new BaseModel<>(newValue);
        }
        String modelJson = getConverterFactory().fromType(type).convert(model);
        sharedPreferences.edit().putString(key, Utils.endecode(modelJson)).apply();
    }

    final boolean remove(String name, String id, String key) {
        if (Hannibai.debug) Log.d(TAG, String.format("Remove the %s in the preferences.", key));
        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
        String value = sharedPreferences.getString(key, null);
        if (value != null && value.length() != 0) {
            if (Hannibai.debug)
                Log.d(TAG, String.format("Find the %s in the preferences.", key));
            return sharedPreferences.edit().remove(key).commit();
        } else {
            if (Hannibai.debug)
                Log.d(TAG, String.format("Don`t find the %s in the preferences.", key));
            return false;
        }
    }

    final boolean clear(String name, String id) {
        if (Hannibai.debug) Log.d(TAG, "Clear the preferences.");
        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
        return sharedPreferences.edit().clear().commit();
    }

    final ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {

            @Override
            public Type getRawType() {
                return raw;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    /**
     * Set converter factory for serialization and deserialization of objects.
     */
    final void setConverterFactory(Converter.Factory factory) {
        converterFactory = (checkNotNull(factory, "factory == null"));
    }

    final Converter.Factory getConverterFactory() {
        return checkNotNull(converterFactory, "factory == null");
    }

}
