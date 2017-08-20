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
            BaseModel<T> model = (BaseModel<T>) getConverterFactory().toType(type).convert(value);
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
        sharedPreferences.edit().putString(key, modelJson).apply();
    }

    final ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    /**
     * Set converter factory for serialization and deserialization of objects.
     */
    void setConverterFactory(Converter.Factory factory) {
        converterFactory = (checkNotNull(factory, "factory == null"));
    }

    Converter.Factory getConverterFactory() {
        return checkNotNull(converterFactory, "factory == null");
    }

}
