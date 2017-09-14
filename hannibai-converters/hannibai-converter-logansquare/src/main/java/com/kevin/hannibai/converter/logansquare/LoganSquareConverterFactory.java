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
package com.kevin.hannibai.converter.logansquare;

import com.bluelinelabs.logansquare.LoganSquare;
import com.kevin.hannibai.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2017/9/14.
 */

public class LoganSquareConverterFactory implements Converter.Factory {

    public static LoganSquareConverterFactory create() {
        return new LoganSquareConverterFactory();
    }

    @Override
    public <F> Converter<F, String> fromType(Type fromType) {
        return new Converter<F, String>() {
            @Override
            public String convert(F value) throws IOException {
                return LoganSquare.serialize(value);
            }
        };
    }

    @Override
    public <T> Converter<String, T> toType(final Type toType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String value) throws IOException {
                return (T) LoganSquare.parse(value, (Class) toType);
            }
        };
    }
}
