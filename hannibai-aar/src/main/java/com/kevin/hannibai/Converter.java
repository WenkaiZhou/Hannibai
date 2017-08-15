package com.kevin.hannibai;

import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public interface Converter<F, T> {

    T convert(F value);

    interface Factory {
        <F> Converter<F, String> fromType(Type fromType);

        <T> Converter<String, T> toType(Type toType);
    }

}
