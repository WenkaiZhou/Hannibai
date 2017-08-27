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
                Log.d(TAG, String.format("Value of %s is empty, return the default %s.", key, defValue));
            return defValue;
        } else {
            ParameterizedType type = type(BaseModel.class, defValue.getClass());
            BaseModel<T> model = (BaseModel<T>) getConverterFactory().toType(type).convert(Utils.endecode(value));
            if (Hannibai.debug) {
                Log.d(TAG, String.format("Value of %s is %s, create at %s, update at %s.", key, model.data, model.createTime, model.updateTime));
                if (!model.isExpired()) {
                    Log.d(TAG, String.format("Value of %s is %s, Will expire after %s seconds.", key, model.data, (model.expireTime.getTime() - System.currentTimeMillis()) / 1000));
                }
            }
            if (model.isExpired()) {
                if (Hannibai.debug)
                    Log.d(TAG, String.format("Value of %s is %s expired, return the default %s.", key, model.data, defValue));
                return defValue;
            } else {
                return model.data;
            }
        }
    }

    final <T> void set1(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
        set(name, id, key, expire, updateExpire, newValue).apply();
    }

    final <T> boolean set2(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
        return set(name, id, key, expire, updateExpire, newValue).commit();
    }

    private final <T> SharedPreferences.Editor set(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
        if (Hannibai.debug) Log.d(TAG, String.format("Set the %s value to the preferences.", key));
        BaseModel<T> model;
        ParameterizedType type = type(BaseModel.class, newValue.getClass());
        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
        String value = sharedPreferences.getString(key, null);
        if (value != null && value.length() != 0) {
            model = (BaseModel<T>) getConverterFactory().toType(type).convert(Utils.endecode(value));
            if (model.isExpired()) {
                model = new BaseModel<>(newValue, expire);
                if (Hannibai.debug)
                    Log.d(TAG, String.format("Value of %s is %s expired", key, model.data));
            } else {
                model.update(newValue, updateExpire);
            }
        } else {
            model = new BaseModel<>(newValue, expire);
        }
        String modelJson = getConverterFactory().fromType(type).convert(model);
        return sharedPreferences.edit().putString(key, Utils.endecode(modelJson));
    }

    final void remove1(String name, String id, String key) {
        remove(name, id, key).apply();
    }

    final boolean remove2(String name, String id, String key) {
        return remove(name, id, key).commit();
    }

    private final SharedPreferences.Editor remove(String name, String id, String key) {
        if (Hannibai.debug) Log.d(TAG, String.format("Remove the %s in the preferences.", key));
        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
        return sharedPreferences.edit().remove(key);
    }

    final void clear(String name, String id) {
        if (Hannibai.debug) Log.d(TAG, "Clear the preferences.");
        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
        sharedPreferences.edit().clear().apply();
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
