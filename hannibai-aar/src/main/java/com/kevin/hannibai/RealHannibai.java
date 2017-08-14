package com.kevin.hannibai;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

class RealHannibai {

    private static final String TAG = "Hannibai";

    private Context mContext;

    private RealHannibai() {
    }

    public static RealHannibai getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RealHannibai INSTANCE = new RealHannibai();
    }

    private final Map<Class<?>, Object> preferenceCache = new ConcurrentHashMap<>();

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public <T> T create(final Class<T> preference) {
        Utils.validatePreferenceInterface(preference);
        return loadProxy(preference);
    }

    private <T> T loadProxy(final Class<T> preference) {
        T result = (T) preferenceCache.get(preference);
        if (result != null) {
            return result;
        }

        synchronized (preferenceCache) {
            result = (T) preferenceCache.get(preference);
            if (result == null) {
                result = createProxy(preference);
                preferenceCache.put(preference, result);
            }
        }

        return result;
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
//                        if (platform.isDefaultMethod(method)) {
//                            return platform.invokeDefaultMethod(method, service, proxy, args);
//                        }
//                        ServiceMethod<Object, Object> serviceMethod =
//                                (ServiceMethod<Object, Object>) loadServiceMethod(method);
//                        OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
//                        return serviceMethod.callAdapter.adapt(okHttpCall);
                        Log.d(TAG, "invoke() called with: proxy = [" + proxy + "], method = [" + method + "], args = [" + args + "]");
                        return true;
                    }
                });
    }
}
