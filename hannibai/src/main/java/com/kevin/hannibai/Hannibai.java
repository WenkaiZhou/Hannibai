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

import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public final class Hannibai {

    static boolean debug = false;

    public static final void init(Context context) {
        RealHannibai.getInstance().init(context, true);
    }

    public static final void init(Context context, boolean encrypt) {
        RealHannibai.getInstance().init(context, encrypt);
    }

    /**
     * Control whether debug logging is enabled.
     */
    public static final void setDebug(boolean debug) {
        Hannibai.debug = debug;
    }

    public static final <T> T create(final Class<T> preference) {
        return RealHannibai.getInstance().create(preference);
    }

    public static final <T> T create(final Class<T> preference, String id) {
        return RealHannibai.getInstance().create(preference, id);
    }

    public static final void setConverterFactory(Converter.Factory factory) {
        RealHannibai.getInstance().setConverterFactory(factory);
    }

    public static final <T> T get1(String name, String id, String key, T defValue) {
        return RealHannibai.getInstance().get(name, id, key, defValue, defValue.getClass());
    }

    public static final <T> T get2(String name, String id, String key, Type type) {
        return RealHannibai.getInstance().get(name, id, key, null, type);
    }

    public static final <T> void set1(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
        RealHannibai.getInstance().set1(name, id, key, expire, updateExpire, newValue);
    }

    public static final <T> boolean set2(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
        return RealHannibai.getInstance().set2(name, id, key, expire, updateExpire, newValue);
    }

    public static final void remove1(String name, String id, String key) {
        RealHannibai.getInstance().remove1(name, id, key);
    }

    public static final boolean remove2(String name, String id, String key) {
        return RealHannibai.getInstance().remove2(name, id, key);
    }

    public static final void clear(String name, String id) {
        RealHannibai.getInstance().clear(name, id);
    }

}
