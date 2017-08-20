package com.kevin.hannibai;

import android.content.Context;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public final class Hannibai {

    static boolean debug = false;

    public static final void init(Context context) {
        RealHannibai.getInstance().init(context);
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

    public static final void setConverterFactory(Converter.Factory factory) {
        RealHannibai.getInstance().setConverterFactory(factory);
    }

    public static final <T> T get(String name, String id, String key, T defValue) {
        return RealHannibai.getInstance().get(name, id, key, defValue);
    }

    public static final <T> void set(String name, String id, String key, T newValue) {
        RealHannibai.getInstance().set(name, id, key, newValue);
    }

    public static final void remove(String name, String id, String key) {
        RealHannibai.getInstance().remove(name, id, key);
    }

    public static final boolean clear(String name, String id) {
        return RealHannibai.getInstance().clear(name, id);
    }

}
