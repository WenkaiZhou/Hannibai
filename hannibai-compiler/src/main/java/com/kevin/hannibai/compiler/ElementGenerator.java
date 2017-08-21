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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.lang.model.element.TypeElement;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

public abstract class ElementGenerator implements Generator {

    protected final TypeElement mElement;
    protected final String mPackageName;
    protected final String mClassName;

    public ElementGenerator(TypeElement element, String classNameSuffix) {
        this(element,
                element.getEnclosingElement().toString(),
                element.getSimpleName().toString() + classNameSuffix);
    }

    public ElementGenerator(TypeElement element, String packageName, String className) {
        this.mElement = element;
        this.mPackageName = packageName;
        this.mClassName = className;
    }

    @Override
    public void generate() {
        try {
            JavaFile.builder(mPackageName, onCreateTypeSpec(mElement, mPackageName, mClassName))
                    .build()
                    .writeTo(EnvironmentManager.getManager().getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract TypeSpec onCreateTypeSpec(TypeElement element, String packageName, String className);
}
