package com.kevin.hannibai;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.kevin.hannibai.Utils.checkNotNull;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

class RealHannibai {

    private static final String TAG = "RealHannibai";

    private Context mContext;

    private RealHannibai() {
    }

    public static RealHannibai getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RealHannibai INSTANCE = new RealHannibai();
    }

    private final Map<Method, HandleMethod> handleMethodCache = new ConcurrentHashMap<>();
    final List<Converter.Factory> converterFactories = new ArrayList<>();

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public <T> T create(final Class<T> preference) {
        Utils.validatePreferenceInterface(preference);
        return createProxy(preference);
    }

    private <T> T createProxy(final Class<T> preference) {
        return (T) Proxy.newProxyInstance(preference.getClassLoader(), new Class<?>[]{preference},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        // If the method is a method from Object then defer to normal invocation.
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }


                        HandleMethod handleMethod = loadHandleMethod(method);
                        Log.d(TAG, "invoke() called with: proxy = [" + proxy + "], method = [" + method + "], args = [" + args + "]");
                        return true;
                    }
                });
    }

    HandleMethod loadHandleMethod(Method method) {
        HandleMethod result = handleMethodCache.get(method);
        if (result != null) return result;

        synchronized (handleMethodCache) {
            result = handleMethodCache.get(method);
            if (result == null) {
                result = new HandleMethod.Builder(this, method).build();
//                handleMethodCache.put(method, result);
            }
        }
        return result;
    }

    /**
     * Add converter factory for serialization and deserialization of objects.
     */
    void addConverterFactory(Converter.Factory factory) {
        converterFactories.add(checkNotNull(factory, "factory == null"));
    }

    List<Converter.Factory> converterFactories() {
        return converterFactories;
    }
}
