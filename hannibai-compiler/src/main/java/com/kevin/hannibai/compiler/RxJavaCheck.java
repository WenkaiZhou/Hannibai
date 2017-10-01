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
package com.kevin.hannibai.compiler;

/**
 * Created by zhouwenkai on 2017/9/29.
 */

final class RxJavaCheck {

    public static final String RXJAVA_1 = "RXJAVA_1";
    public static final String RXJAVA_2 = "RXJAVA_2";

    public static final String RXJAVA = findRxJava();

    private static String findRxJava() {
        try {
            Class.forName("io.reactivex.Observable");
            return RXJAVA_2;
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("rx.Observable");
            return RXJAVA_1;
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }
}
