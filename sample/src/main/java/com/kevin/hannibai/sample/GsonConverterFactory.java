package com.kevin.hannibai.sample;

import com.google.gson.Gson;
import com.kevin.hannibai.Converter;

import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public class GsonConverterFactory implements Converter.Factory {

    private GsonConverterFactory() {

    }

    public static GsonConverterFactory create() {
        return new GsonConverterFactory();
    }

    @Override
    public <F> Converter<F, String> fromType(Type fromType) {
        return new Converter<F, String>() {
            @Override
            public String convert(F value) {
                return new Gson().toJson(value);
            }
        };
    }

    @Override
    public <T> Converter<String, T> toType(final Type toType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String value) {
                return new Gson().fromJson(value, toType);
            }
        };
    }
}
