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
import com.squareup.javapoet.FieldSpec;
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
import static com.kevin.hannibai.compiler.Constants.HANNIBAI;
import static com.kevin.hannibai.compiler.Constants.PACKAGE_NAME;
import static com.kevin.hannibai.compiler.Constants.PUT_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.REMOVE;
import static com.kevin.hannibai.compiler.Constants.REMOVE_ALL;
import static com.kevin.hannibai.compiler.Constants.REMOVE_ALL_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.REMOVE_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.SET;

/**
 * Created by zhouwenkai on 2017/8/19.
 */

public class HandleImplGenerator extends ElementGenerator {

    private String mSuperinterface;
    private String mSharedPreferencesName;

    public HandleImplGenerator(TypeElement element, String classNameSuffix, String superinterface) {
        super(element, classNameSuffix);
        this.mSuperinterface = superinterface;
        this.mSharedPreferencesName = element.toString();
    }

    @Override
    public TypeSpec onCreateTypeSpec(TypeElement element, String packageName, String className) {
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        HashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();


        // Add getInstance method
        methodSpecs.add(
                MethodSpec.methodBuilder("getInstance")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(ClassName.get(packageName, className))
                        .addStatement("return Holder.INSTANCE")
                        .build()
        );

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

                Object defValue = HannibaiUtils.getDefValue(enclosedElement, enclosedElement.asType().toString());
                String expireValue = HannibaiUtils.getExpireValue(enclosedElement);
                boolean expireUpdate = HannibaiUtils.getExpireUpdate(enclosedElement);

                // The get method
                methodSpecs.add(
                        MethodSpec.methodBuilder(GET + formatName)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(ClassName.get(enclosedElement.asType()))
                                .addAnnotations(defValueAnnotation)
                                .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName())
                                )
                                .addStatement("return $T.get($N, $L, $S, $L)",
                                        ClassName.get(PACKAGE_NAME, HANNIBAI),
                                        "mSharedPreferencesName",
                                        "mId",
                                        enclosedElement.getSimpleName(),
                                        defValue)
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
                        enclosedElement.getAnnotation(Commit.class) == null ?
                                MethodSpec.methodBuilder(SET + formatName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(ClassName.get(enclosedElement.asType()),
                                                enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                                        .returns(returnType)
                                        .addAnnotations(setMethodAnnotations)
                                        .addJavadoc(String.format(PUT_METHOD_JAVA_DOC,
                                                enclosedElement.getSimpleName(),
                                                enclosedElement.getSimpleName())
                                        )
                                        .addStatement("$T.set1($N, $L, $S, $L, $L, $L)",
                                                ClassName.get(PACKAGE_NAME, HANNIBAI),
                                                "mSharedPreferencesName",
                                                "mId",
                                                enclosedElement.getSimpleName(),
                                                expireValue,
                                                expireUpdate,
                                                enclosedElement.getSimpleName())
                                        .build()
                                :
                                MethodSpec.methodBuilder(SET + formatName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(ClassName.get(enclosedElement.asType()),
                                                enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                                        .returns(returnType)
                                        .addAnnotations(setMethodAnnotations)
                                        .addJavadoc(String.format(PUT_METHOD_JAVA_DOC,
                                                enclosedElement.getSimpleName(),
                                                enclosedElement.getSimpleName())
                                        )
                                        .addStatement("return $T.set2($N, $L, $S, $L, $L, $L)",
                                                ClassName.get(PACKAGE_NAME, HANNIBAI),
                                                "mSharedPreferencesName",
                                                "mId",
                                                enclosedElement.getSimpleName(),
                                                expireValue,
                                                expireUpdate,
                                                enclosedElement.getSimpleName())
                                        .build()
                );

                // The remove method
                methodSpecs.add(
                        enclosedElement.getAnnotation(Commit.class) == null ?
                                MethodSpec.methodBuilder(REMOVE + formatName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(returnType)
                                        .addAnnotation(submitAnnotation)
                                        .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                                                enclosedElement.getSimpleName())
                                        )
                                        .addStatement("$T.remove1($N, $L, $S)",
                                                ClassName.get(PACKAGE_NAME, HANNIBAI),
                                                "mSharedPreferencesName",
                                                "mId",
                                                enclosedElement.getSimpleName())
                                        .build()
                                :
                                MethodSpec.methodBuilder(REMOVE + formatName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(returnType)
                                        .addAnnotation(submitAnnotation)
                                        .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                                                enclosedElement.getSimpleName())
                                        )
                                        .addStatement("return $T.remove2($N, $L, $S)",
                                                ClassName.get(PACKAGE_NAME, HANNIBAI),
                                                "mSharedPreferencesName",
                                                "mId",
                                                enclosedElement.getSimpleName())
                                        .build()
                );
            }
        }

        if (methodSpecs.size() > 0) {
            // The deleteAll method
            MethodSpec methodDelete = MethodSpec.methodBuilder(REMOVE_ALL)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(AnnotationSpec.builder(Apply.class).build())
                    .addJavadoc(REMOVE_ALL_METHOD_JAVA_DOC)
                    .addStatement("$T.clear($N, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId")
                    .build();
            methodSpecs.add(methodDelete);
        }

        // Add constructor method
        methodSpecs.add(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addStatement("this.$N = $S", "mId", "")
                .build());

        methodSpecs.add(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(String.class), "id")
                .addStatement("this.$N = $L", "mId", "id")
                .build());

        // Holder class
        TypeSpec holder = TypeSpec.classBuilder("Holder")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addField(FieldSpec.builder(ClassName.get(packageName, className), "INSTANCE")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T()", ClassName.get(packageName, className))
                        .build())
                .build();

        // Field
        HashSet<FieldSpec> fieldSpecs = new LinkedHashSet<>();
        fieldSpecs.add(FieldSpec
                .builder(ClassName.get(String.class), "mSharedPreferencesName", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", mSharedPreferencesName)
                .build());

        fieldSpecs.add(FieldSpec
                .builder(ClassName.get(String.class), "mId", Modifier.PRIVATE, Modifier.FINAL)
                .build());

        return TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(packageName, mSuperinterface))
                .addModifiers(Modifier.FINAL)
                .addType(holder)
                .addMethods(methodSpecs)
                .addFields(fieldSpecs)
                .addJavadoc(CLASS_JAVA_DOC)
                .build();
    }
}
