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

import com.kevin.hannibai.annotation.DefBoolean;
import com.kevin.hannibai.annotation.DefFloat;
import com.kevin.hannibai.annotation.DefInt;
import com.kevin.hannibai.annotation.DefLong;
import com.kevin.hannibai.annotation.DefString;
import com.kevin.hannibai.annotation.Expire;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.lang.model.element.Element;

/**
 * Created by zhouwenkai on 2017/8/19.
 */

final class HannibaiUtils {

    /**
     * Create DefValue AnnotationSpec
     *
     * @param element
     * @param typeName
     * @return
     */
    static Iterable<AnnotationSpec> createDefValueAnnotation(Element element, String typeName) {
        HashSet<AnnotationSpec> annotationSpecs = new LinkedHashSet<>();
        if (typeName.equals(int.class.getName())
                || typeName.equals(Integer.class.getName())) {
            DefInt defInt = element.getAnnotation(DefInt.class);
            annotationSpecs.add(
                    AnnotationSpec.builder(DefInt.class)
                            .addMember("value", "$L", defInt == null ? 0 : defInt.value())
                            .build());
        } else if (typeName.equals(String.class.getName())) {
            DefString defString = element.getAnnotation(DefString.class);
            annotationSpecs.add(
                    AnnotationSpec.builder(DefString.class)
                            .addMember("value", "$S", defString == null ? "" : defString.value())
                            .build());
        } else if (typeName.equals(boolean.class.getName())
                || typeName.equals(Boolean.class.getName())) {
            DefBoolean defBoolean = element.getAnnotation(DefBoolean.class);
            annotationSpecs.add(
                    AnnotationSpec.builder(DefBoolean.class)
                            .addMember("value", "$L", defBoolean == null ? false : defBoolean.value())
                            .build());
        } else if (typeName.equals(long.class.getName())
                || typeName.equals(Long.class.getName())) {
            DefLong defLong = element.getAnnotation(DefLong.class);
            annotationSpecs.add(
                    AnnotationSpec.builder(DefLong.class)
                            .addMember("value", "$LL", defLong == null ? 0 : defLong.value())
                            .build());
        } else if (typeName.equals(float.class.getName())
                || typeName.equals(Float.class.getName())) {
            DefFloat defFloat = element.getAnnotation(DefFloat.class);
            annotationSpecs.add(
                    AnnotationSpec.builder(DefFloat.class)
                            .addMember("value", "$LF", defFloat == null ? 0 : defFloat.value())
                            .build());
        }
        return annotationSpecs;
    }

    /**
     * Get DefValue
     *
     * @param element
     * @param typeName
     * @return
     */
    static Object getDefValue(Element element, String typeName) {
        if (typeName.equals(int.class.getName())
                || typeName.equals(Integer.class.getName())) {
            DefInt defInt = element.getAnnotation(DefInt.class);
            return defInt == null ? 0 : defInt.value();
        } else if (typeName.equals(String.class.getName())) {
            DefString defString = element.getAnnotation(DefString.class);
            return defString == null ? "\"\"" : "\"" + defString.value() + "\"";
        } else if (typeName.equals(boolean.class.getName())
                || typeName.equals(Boolean.class.getName())) {
            DefBoolean defBoolean = element.getAnnotation(DefBoolean.class);
            return defBoolean == null ? false : defBoolean.value();
        } else if (typeName.equals(long.class.getName())
                || typeName.equals(Long.class.getName())) {
            DefLong defLong = element.getAnnotation(DefLong.class);
            return defLong == null ? 0 + "L" : defLong.value() + "L";
        } else if (typeName.equals(float.class.getName())
                || typeName.equals(Float.class.getName())) {
            DefFloat defFloat = element.getAnnotation(DefFloat.class);
            return defFloat == null ? 0 + "F" : defFloat.value() + "F";
        } else {
            return null;
        }
    }

    /**
     * Get the expire AnnotationSpec
     *
     * @param element
     * @return
     */
    static AnnotationSpec getExpireAnnotation(Element element) {
        Expire expire = element.getAnnotation(Expire.class);
        if (expire != null) {
            return AnnotationSpec.builder(Expire.class)
                    .addMember("value", "$LL", expire.value())
                    .addMember("update", "$L", expire.update())
                    .addMember("unit", "$T.$L", ClassName.get(Expire.Unit.class), expire.unit())
                    .build();
        } else {
            return null;
        }

    }

    /**
     * Get the expire time
     *
     * @param element
     * @return
     */
    static String getExpireValue(Element element) {
        Expire expire = element.getAnnotation(Expire.class);
        if (expire != null) {
            return String.format("%dL * %s", expire.value(), expire.unit().getValue());
        } else {
            return "-1L";
        }
    }

    /**
     * Whether update the expire time
     *
     * @param element
     * @return
     */
    static boolean getExpireUpdate(Element element) {
        Expire expire = element.getAnnotation(Expire.class);
        if (expire != null) {
            return expire.update();
        } else {
            return false;
        }
    }
}
