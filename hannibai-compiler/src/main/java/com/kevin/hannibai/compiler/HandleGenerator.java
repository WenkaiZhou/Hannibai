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
import com.kevin.hannibai.annotation.Commit;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.kevin.hannibai.compiler.Constants.CLASS_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.GET;
import static com.kevin.hannibai.compiler.Constants.GET_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.PUT_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.REMOVE;
import static com.kevin.hannibai.compiler.Constants.REMOVE_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.SET;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

class HandleGenerator extends ElementGenerator {

    public HandleGenerator(TypeElement element, String classNameSuffix) {
        super(element, classNameSuffix);
    }

    @Override
    public TypeSpec onCreateTypeSpec(TypeElement element, String packageName, String className) {
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        HashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();
        for (Element enclosedElement : enclosedElements) {

            if (enclosedElement.getKind() == ElementKind.FIELD) {
                String formatName = Utils.capitalize(enclosedElement.getSimpleName());

                Iterable<AnnotationSpec> defValueAnnotation = HannibaiUtils.createDefValueAnnotation(
                        enclosedElement, enclosedElement.asType().toString());

                TypeName returnType;
                AnnotationSpec submitAnnotation;
                if (enclosedElement.getAnnotation(Commit.class) == null) {
                    submitAnnotation = AnnotationSpec.builder(Apply.class).build();
                    returnType = TypeName.VOID;
                } else {
                    submitAnnotation = AnnotationSpec.builder(Commit.class).build();
                    returnType = TypeName.BOOLEAN;
                }

                // The get method
                methodSpecs.add(
                        MethodSpec.methodBuilder(GET + formatName)
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(ClassName.get(enclosedElement.asType()))
                                .addAnnotations(defValueAnnotation)
                                .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName())
                                )
                                .build()
                );

                HashSet<AnnotationSpec> setMethodAnnotations = new LinkedHashSet<>();
                setMethodAnnotations.add(submitAnnotation);
                AnnotationSpec expireAnnotation = HannibaiUtils.getExpireAnnotation(enclosedElement);
                if (expireAnnotation != null) {
                    setMethodAnnotations.add(expireAnnotation);
                }

                // The set method
                methodSpecs.add(
                        MethodSpec.methodBuilder(SET + formatName)
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addParameter(ClassName.get(enclosedElement.asType()),
                                        enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                                .addAnnotations(setMethodAnnotations)
                                .returns(returnType)
                                .addJavadoc(String.format(PUT_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName(),
                                        enclosedElement.getSimpleName())
                                )
                                .build()
                );

                // The remove method
                methodSpecs.add(
                        MethodSpec.methodBuilder(REMOVE + formatName)
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(returnType)
                                .addAnnotation(submitAnnotation)
                                .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName())
                                )
                                .build()
                );
            }
        }

        return TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addSuperinterface(ClassName.get(Constants.PACKAGE_NAME, Constants.IHANDLE))
                .addJavadoc(CLASS_JAVA_DOC)
                .build();
    }

}
