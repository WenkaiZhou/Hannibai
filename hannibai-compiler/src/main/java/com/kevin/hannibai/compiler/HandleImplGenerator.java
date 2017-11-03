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
import com.kevin.hannibai.annotation.RxJava;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
import static com.kevin.hannibai.compiler.Constants.CONTAINS;
import static com.kevin.hannibai.compiler.Constants.CONTAINS_METHOD_JAVA_DOC;
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

        // Field
        HashSet<FieldSpec> fieldSpecs = new LinkedHashSet<>();
        fieldSpecs.add(FieldSpec
                .builder(TypeName.get(String.class), "mSharedPreferencesName", Modifier.PRIVATE, Modifier.FINAL)
                .initializer("$S", mSharedPreferencesName)
                .build());

        fieldSpecs.add(FieldSpec
                .builder(ClassName.get(String.class), "mId", Modifier.PRIVATE, Modifier.FINAL)
                .build());

        // Add constructor method
        methodSpecs.add(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addStatement("this.$N = $S", "mId", "")
                .build());

        methodSpecs.add(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(String.class), "id")
                .addStatement("this.$N = $L", "mId", "id")
                .build());

        // Add getInstance method
        methodSpecs.add(
                MethodSpec.methodBuilder("getInstance")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(ClassName.get(packageName, className))
                        .addStatement("return Holder.INSTANCE")
                        .build()
        );

        // Holder class
        TypeSpec holder = TypeSpec.classBuilder("Holder")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addField(FieldSpec.builder(ClassName.get(packageName, className), "INSTANCE")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T()", ClassName.get(packageName, className))
                        .build())
                .build();


        for (Element enclosedElement : enclosedElements) {

            if (enclosedElement.getKind() == ElementKind.FIELD) {
                String formatName = Utils.capitalize(enclosedElement.getSimpleName());

                AnnotationSpec submitAnnotation;
                if (enclosedElement.getAnnotation(Commit.class) == null) {
                    submitAnnotation = AnnotationSpec.builder(Apply.class).build();
                } else {
                    submitAnnotation = AnnotationSpec.builder(Commit.class).build();
                }

                // get the default value annotation
                Iterable<AnnotationSpec> defValueAnnotation = HannibaiUtils
                        .createDefValueAnnotation(enclosedElement, enclosedElement.asType().toString());

                methodSpecs.add(createContainsMethodSpec(enclosedElement, defValueAnnotation, formatName));

                // The get method
                methodSpecs.add(createGetMethodSpec(enclosedElement, defValueAnnotation, formatName));

                MethodSpec observableGetMethod = createObservableGetMethodSpec(
                        enclosedElement, defValueAnnotation, formatName);
                if (null != observableGetMethod) {
                    methodSpecs.add(observableGetMethod);
                }

                // The set method
                methodSpecs.add(createSetMethodSpec(enclosedElement, formatName, submitAnnotation));

                // The remove method
                methodSpecs.add(createRemoveMethodSpec(enclosedElement, formatName, submitAnnotation));
            }
        }

//        if (methodSpecs.size() > 0) {
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
//        }

        return TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(packageName, mSuperinterface))
                .addSuperinterface(ClassName.get(Constants.PACKAGE_NAME, Constants.IHANDLE))
                .addModifiers(Modifier.FINAL)
                .addType(holder)
                .addMethods(methodSpecs)
                .addFields(fieldSpecs)
                .addJavadoc(CLASS_JAVA_DOC)
                .build();
    }

    private MethodSpec createContainsMethodSpec(Element enclosedElement,
                                                Iterable<AnnotationSpec> defValueAnnotation,
                                                String formatName) {
        // get the default value
        Object defValue = HannibaiUtils.getDefValue(enclosedElement, enclosedElement.asType().toString());
        // get the Element TypeName
        TypeName typeName = TypeName.get(enclosedElement.asType());

        // has the default value
        if (null != defValue) {
            return MethodSpec.methodBuilder(CONTAINS + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.BOOLEAN)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(CONTAINS_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .addStatement("return $T.contains1($N, $L, $S, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName(),
                            defValue)
                    .build();
        } else if (typeName instanceof ClassName) {
            String type = Utils.join(".", ((ClassName) typeName).simpleNames()) + ".class";
            return MethodSpec.methodBuilder(CONTAINS + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.BOOLEAN)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(CONTAINS_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .addStatement("return $T.contains2($N, $L, $S, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName(),
                            type)
                    .build();
        } else if (typeName instanceof ParameterizedTypeName) {
            String rawType = Utils.join(".", ((ParameterizedTypeName) typeName).rawType.simpleNames()) + ".class";

            StringBuffer types = new StringBuffer();
            List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
            for (int i = 0; i < typeArguments.size(); i++) {
                if (typeArguments.get(i) instanceof ClassName) {
                    types.append((Utils.join(".", ((ClassName) typeArguments.get(i)).simpleNames()) + ".class"));
                    if (i != typeArguments.size() - 1) {
                        types.append(", ");
                    }
                } else {
                    Utils.error(enclosedElement, "don`t support!!!");
                }
            }

            return MethodSpec.methodBuilder(CONTAINS + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.BOOLEAN)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(CONTAINS_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .beginControlFlow("$T type = new $T()",
                            ClassName.get("java.lang.reflect", "Type"),
                            ClassName.get("java.lang.reflect", "ParameterizedType"))

                    .addCode("@Override\n")
                    .beginControlFlow("public Type getRawType()")
                    .addStatement("return " + rawType)
                    .endControlFlow()

                    .addCode("@Override\n")
                    .beginControlFlow("public Type[] getActualTypeArguments()")
                    .addStatement("return new Type[]{" + types + "}")
                    .endControlFlow()

                    .addCode("@Override\n")
                    .beginControlFlow("public Type getOwnerType()")
                    .addStatement("return null")
                    .endControlFlow()

                    .endControlFlow("")
                    .addStatement("return $T.contains2($N, $L, $S, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName(),
                            "type")
                    .build();
        } else {
            Utils.error(enclosedElement, "don`t support!!!");
            return null;
        }
    }

    private MethodSpec createGetMethodSpec(Element enclosedElement,
                                           Iterable<AnnotationSpec> defValueAnnotation,
                                           String formatName) {
        // get the default value
        Object defValue = HannibaiUtils.getDefValue(enclosedElement, enclosedElement.asType().toString());
        // get the Element TypeName
        TypeName typeName = TypeName.get(enclosedElement.asType());

        // has the default value
        if (null != defValue) {
            return MethodSpec.methodBuilder(GET + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .addStatement("return $T.get1($N, $L, $S, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName(),
                            defValue)
                    .build();
        } else if (typeName instanceof ClassName) {
            String type = Utils.join(".", ((ClassName) typeName).simpleNames()) + ".class";

            return MethodSpec.methodBuilder(GET + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .addStatement("return $T.get2($N, $L, $S, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName(),
                            type)
                    .build();
        } else if (typeName instanceof ParameterizedTypeName) {
            String rawType = Utils.join(".", ((ParameterizedTypeName) typeName).rawType.simpleNames()) + ".class";

            StringBuffer types = new StringBuffer();
            List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
            for (int i = 0; i < typeArguments.size(); i++) {
                if (typeArguments.get(i) instanceof ClassName) {
                    types.append((Utils.join(".", ((ClassName) typeArguments.get(i)).simpleNames()) + ".class"));
                    if (i != typeArguments.size() - 1) {
                        types.append(", ");
                    }
                } else {
                    Utils.error(enclosedElement, "don`t support!!!");
                }
            }

            return MethodSpec.methodBuilder(GET + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(enclosedElement.asType()))
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .beginControlFlow("$T type = new $T()",
                            ClassName.get("java.lang.reflect", "Type"),
                            ClassName.get("java.lang.reflect", "ParameterizedType"))

                    .addCode("@Override\n")
                    .beginControlFlow("public Type getRawType()")
                    .addStatement("return " + rawType)
                    .endControlFlow()

                    .addCode("@Override\n")
                    .beginControlFlow("public Type[] getActualTypeArguments()")
                    .addStatement("return new Type[]{" + types + "}")
                    .endControlFlow()

                    .addCode("@Override\n")
                    .beginControlFlow("public Type getOwnerType()")
                    .addStatement("return null")
                    .endControlFlow()

                    .endControlFlow("")
                    .addStatement("return $T.get2($N, $L, $S, $L)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName(),
                            "type")
                    .build();

        } else {
            Utils.error(enclosedElement, "don`t support!!!");
            return null;
        }
    }

    private MethodSpec createObservableGetMethodSpec(Element enclosedElement,
                                                     Iterable<AnnotationSpec> defValueAnnotation,
                                                     String formatName) {

        RxJava rxJava = HannibaiUtils.checkRxJava(enclosedElement);

        if (rxJava == null) {
            return null;
        }

        if (rxJava.value() == RxJava.Version.RXJAVA2) {

            ParameterizedTypeName typeName = ParameterizedTypeName
                    .get(ClassName.get("io.reactivex", "Observable"),
                            TypeName.get(enclosedElement.asType()).box());

            return MethodSpec.methodBuilder(GET + formatName + rxJava.suffix())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )

                    .beginControlFlow("return $T.create(new $T<$T>()",
                            ClassName.bestGuess("io.reactivex.Observable"),
                            ClassName.bestGuess("io.reactivex.ObservableOnSubscribe"),
                            TypeName.get(enclosedElement.asType()).box())
                    .addCode("@Override\n")
                    .beginControlFlow("public void subscribe($T<$T> e) throws $T",
                            ClassName.bestGuess("io.reactivex.ObservableEmitter"),
                            TypeName.get(enclosedElement.asType()).box(),
                            ClassName.bestGuess("java.lang.Exception"))
                    .addStatement("e.onNext($L)", GET + formatName + "()")
                    .addStatement("e.onComplete()")
                    .endControlFlow()
                    .endControlFlow()
                    .addCode(")\n")
                    .addCode(".subscribeOn($T.io())\n",
                            ClassName.bestGuess("io.reactivex.schedulers.Schedulers"))
                    .addStatement(".observeOn($T.mainThread())",
                            ClassName.bestGuess("io.reactivex.android.schedulers.AndroidSchedulers"))
                    .build();
        } else {
            ParameterizedTypeName typeName = ParameterizedTypeName
                    .get(ClassName.bestGuess("rx.Observable"),
                            TypeName.get(enclosedElement.asType()).box());

            return MethodSpec.methodBuilder(GET + formatName + rxJava.suffix())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName)
                    .addAnnotations(defValueAnnotation)
                    .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .beginControlFlow("return $T.create(new $T<$T>()",
                            ClassName.bestGuess("rx.Observable"),
                            ClassName.bestGuess("rx.Observable.OnSubscribe"),
                            TypeName.get(enclosedElement.asType()).box())
                    .addCode("@Override\n")
                    .beginControlFlow("public void call($T<? super $T> subscriber)",
                            ClassName.bestGuess("rx.Subscriber"),
                            TypeName.get(enclosedElement.asType()).box()
                    )
                    .addStatement("subscriber.onNext($L)", GET + formatName + "()")
                    .addStatement("subscriber.onCompleted()")
                    .endControlFlow()
                    .endControlFlow()
                    .addCode(")\n")
                    .addCode(".subscribeOn($T.io())\n",
                            ClassName.bestGuess("rx.schedulers.Schedulers"))
                    .addStatement(".observeOn($T.mainThread())",
                            ClassName.bestGuess("rx.android.schedulers.AndroidSchedulers"))
                    .build();
        }
    }

    private MethodSpec createSetMethodSpec(Element enclosedElement, String formatName,
                                           AnnotationSpec submitAnnotation) {

        HashSet<AnnotationSpec> setMethodAnnotations = new LinkedHashSet<>();
        setMethodAnnotations.add(submitAnnotation);
        AnnotationSpec expireAnnotation = HannibaiUtils.getExpireAnnotation(enclosedElement);
        if (expireAnnotation != null) {
            setMethodAnnotations.add(expireAnnotation);
        }

        String expireValue = HannibaiUtils.getExpireValue(enclosedElement);
        boolean expireUpdate = HannibaiUtils.getExpireUpdate(enclosedElement);

        if (enclosedElement.getAnnotation(Commit.class) == null) {
            return MethodSpec.methodBuilder(SET + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(enclosedElement.asType()),
                            enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                    .returns(TypeName.VOID)
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
                    .build();


        } else {
            return MethodSpec.methodBuilder(SET + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(enclosedElement.asType()),
                            enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                    .returns(TypeName.BOOLEAN)
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
                    .build();
        }

    }

    private MethodSpec createRemoveMethodSpec(Element enclosedElement, String formatName,
                                              AnnotationSpec submitAnnotation) {
        if (enclosedElement.getAnnotation(Commit.class) == null) {
            return MethodSpec.methodBuilder(REMOVE + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addAnnotation(submitAnnotation)
                    .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .addStatement("$T.remove1($N, $L, $S)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName())
                    .build();
        } else {
            return MethodSpec.methodBuilder(REMOVE + formatName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.BOOLEAN)
                    .addAnnotation(submitAnnotation)
                    .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                            enclosedElement.getSimpleName())
                    )
                    .addStatement("return $T.remove2($N, $L, $S)",
                            ClassName.get(PACKAGE_NAME, HANNIBAI),
                            "mSharedPreferencesName",
                            "mId",
                            enclosedElement.getSimpleName())
                    .build();
        }
    }
}
