package com.kevin.hannibai.compiler;

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

                AnnotationSpec annotationSpec = HannibaiUtils.createDefValueAnnotation(enclosedElement,
                        enclosedElement.asType().toString());

                Object defValue = HannibaiUtils.getDefValue(enclosedElement, enclosedElement.asType().toString());

                // The get method
                methodSpecs.add(
                        annotationSpec == null ?
                                MethodSpec.methodBuilder(GET + formatName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(ClassName.get(enclosedElement.asType()))
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
                                :
                                MethodSpec.methodBuilder(GET + formatName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .returns(ClassName.get(enclosedElement.asType()))
                                        .addAnnotation(annotationSpec)
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

                // The set method
                methodSpecs.add(
                        MethodSpec.methodBuilder(SET + formatName)
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(ClassName.get(enclosedElement.asType()),
                                        enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                                .returns(TypeName.VOID)
                                .addJavadoc(String.format(PUT_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName(),
                                        enclosedElement.getSimpleName())
                                )
                                .addStatement("$T.set($N, $L, $S, $L)",
                                        ClassName.get(PACKAGE_NAME, HANNIBAI),
                                        "mSharedPreferencesName",
                                        "mId",
                                        enclosedElement.getSimpleName(),
                                        enclosedElement.getSimpleName())
                                .build()
                );

                // The remove method
                methodSpecs.add(
                        MethodSpec.methodBuilder(REMOVE + formatName)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.BOOLEAN)
                                .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName())
                                )
                                .addStatement("return $T.remove($N, $L, $S)",
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
                    .returns(TypeName.BOOLEAN)
                    .addJavadoc(REMOVE_ALL_METHOD_JAVA_DOC)
                    .addStatement("return $T.clear($N, $L)",
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
