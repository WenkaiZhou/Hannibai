package com.kevin.hannibai.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhouwenkai on 2017/8/24.
 */

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Expire {

    long value() default 0;

    boolean update() default false;

    Unit unit() default Unit.MILLISECONDS;

    enum Unit {
        MILLISECONDS("1"),
        SECONDS("1000"),
        MINUTES("60 * 1000"),
        HOURS("60 * 60 * 1000"),
        DAYS("24 * 60 * 60 * 1000"),
        FOREVER("-1");

        private final String value;

        Unit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


}
