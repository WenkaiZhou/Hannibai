package com.kevin.hannibai;

import android.content.Context;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public final class Hannibai {

    static boolean debug = false;

    public static void init(Context context) {
        RealHannibai.getInstance().init(context);
    }

    /** Control whether debug logging is enabled. */
    public static void setDebug(boolean debug) {
        Hannibai.debug = debug;
    }

    public static <T> T create(final Class<T> preference) {
        return RealHannibai.getInstance().create(preference);
    }

    public static void setConverterFactory(Converter.Factory factory) {
        RealHannibai.getInstance().setConverterFactory(factory);
    }

    public static <T> T get(String name, String id, String key, T defValue) {
        return RealHannibai.getInstance().get(name, id, key, defValue);
    }

    public static <T> void set(String name, String id, String key, T newValue) {
        RealHannibai.getInstance().set(name, id, key, newValue);
    }

}
