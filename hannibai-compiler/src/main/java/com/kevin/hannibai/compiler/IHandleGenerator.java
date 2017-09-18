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

import com.kevin.hannibai.annotation.Apply;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.lang.model.element.Modifier;

import static com.kevin.hannibai.compiler.Constants.CLASS_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.REMOVE_ALL;
import static com.kevin.hannibai.compiler.Constants.REMOVE_ALL_METHOD_JAVA_DOC;

/**
 * Created by zhouwenkai on 2017/9/18.
 */

public class IHandleGenerator implements Generator {

    @Override
    public void generate() {
        HashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();

        MethodSpec methodDelete = MethodSpec.methodBuilder(REMOVE_ALL)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addAnnotation(AnnotationSpec.builder(Apply.class).build())
                .addJavadoc(REMOVE_ALL_METHOD_JAVA_DOC)
                .build();

        methodSpecs.add(methodDelete);

        TypeSpec typeSpec = TypeSpec.interfaceBuilder(Constants.IHANDLE)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addJavadoc(CLASS_JAVA_DOC)
                .build();

        try {
            JavaFile.builder(Constants.PACKAGE_NAME, typeSpec)
                    .build()
                    .writeTo(EnvironmentManager.getManager().getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
