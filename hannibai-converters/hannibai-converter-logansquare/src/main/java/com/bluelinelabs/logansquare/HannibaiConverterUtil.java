/*
 * Copyright (c) 2018 Kevin zhou
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
package com.bluelinelabs.logansquare;

import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2018/1/3.
 */

public class HannibaiConverterUtil {

    private HannibaiConverterUtil() {
        throw new AssertionError();
    }

    public static ParameterizedType parameterizedTypeOf(Type type) {
        return new ParameterizedType.ConcreteParameterizedType(type);
    }
}
