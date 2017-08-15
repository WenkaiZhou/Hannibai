package com.kevin.hannibai;

import android.util.Log;

import com.kevin.hannibai.annotation.DefBoolean;
import com.kevin.hannibai.annotation.DefFloat;
import com.kevin.hannibai.annotation.DefInt;
import com.kevin.hannibai.annotation.DefLong;
import com.kevin.hannibai.annotation.DefString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public class HandleMethod<T> {

    private static final String TAG = "HandleMethod";

    private final Converter<String, T> converter;

    public HandleMethod(Converter<String, T> converter) {
        this.converter = converter;
    }


    public static class Builder<T> {
        private final RealHannibai realHannibai;
        private final Method method;
        private final Class<?> returnType;

        private final Annotation[] methodAnnotations;
        private final Type[] parameterTypes;
        private Date createTime;
        private Date updateTime;
        private Date expireTime;
        private T data;

        public Builder(RealHannibai realHannibai, Method method) {
            this.realHannibai = realHannibai;
            this.method = method;
            this.returnType = method.getReturnType();

            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
        }

        public HandleMethod build() {
            if (isGetter(method)) {
                Log.d(TAG, method.getName());

                if (int.class.equals(returnType)
                        || Integer.class.equals(returnType)) {
                    DefInt defInt = method.getAnnotation(DefInt.class);

                } else if (String.class.equals(returnType)) {
                    DefString defString = method.getAnnotation(DefString.class);

                } else if (boolean.class.equals(returnType)
                        || Boolean.class.equals(returnType)) {
                    DefBoolean defBoolean = method.getAnnotation(DefBoolean.class);

                } else if (long.class.equals(returnType)
                        || Long.class.equals(returnType)) {
                    DefLong defLong = method.getAnnotation(DefLong.class);

                } else if (float.class.equals(returnType)
                        || Float.class.equals(returnType)) {
                    DefFloat defFloat = method.getAnnotation(DefFloat.class);

                }


            } else if (isSetter(method)) {
// TODO: 2017/8/14  是设置方法
            } else if (isDeleter(method)) {
                // TODO: 2017/8/14 是删除方法
            } else {
                // TODO: 2017/8/14  异常
            }


            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            return null;
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof DefInt) {

            } else if (annotation instanceof DefString) {

            } else if (annotation instanceof DefBoolean) {

            } else if (annotation instanceof DefLong) {

            } else if (annotation instanceof DefFloat) {

            }
        }


        boolean isGetter(Method method) {
            if (!method.getName().startsWith("get")) return false;
            if (method.getParameterTypes().length != 0) return false;
            if (void.class.equals(method.getReturnType())) return false;
            return true;
        }

        boolean isSetter(Method method) {
            if (!method.getName().startsWith("set")) return false;
            if (method.getParameterTypes().length != 1) return false;
            return true;
        }

        boolean isDeleter(Method method) {
            if (!method.getName().startsWith("delete")) return false;
            if (method.getParameterTypes().length != 0) return false;
            return true;
        }
    }
}
