package com.kevin.hannibai.compiler;

import com.kevin.hannibai.annotation.DefBoolean;
import com.kevin.hannibai.annotation.DefFloat;
import com.kevin.hannibai.annotation.DefInt;
import com.kevin.hannibai.annotation.DefLong;
import com.kevin.hannibai.annotation.DefString;
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
import static com.kevin.hannibai.compiler.Constants.REMOVE_ALL;
import static com.kevin.hannibai.compiler.Constants.REMOVE_ALL_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.REMOVE_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.SET;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

public class PreferenceGenerator extends ElementGenerator {

    public PreferenceGenerator(TypeElement element, String classNameSuffix) {
        super(element, classNameSuffix);
    }

    @Override
    public TypeSpec onCreateTypeSpec(TypeElement element, String packageName, String className) {
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        HashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();
        for (Element enclosedElement : enclosedElements) {

            if (enclosedElement.getKind() == ElementKind.FIELD) {
                String formatName = Utils.capitalize(enclosedElement.getSimpleName());

                AnnotationSpec annotationSpec = createAnnotationSpec(enclosedElement,
                        enclosedElement.asType().toString());

                // The get method
                methodSpecs.add(
                        annotationSpec == null ?
                                MethodSpec.methodBuilder(GET + formatName)
                                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                        .returns(ClassName.get(enclosedElement.asType()))
                                        .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                                                enclosedElement.getSimpleName())
                                        )
                                        .build()
                                :
                                MethodSpec.methodBuilder(GET + formatName)
                                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                        .returns(ClassName.get(enclosedElement.asType()))
                                        .addAnnotation(annotationSpec)
                                        .addJavadoc(String.format(GET_METHOD_JAVA_DOC,
                                                enclosedElement.getSimpleName())
                                        )
                                        .build()
                );

                // The set method
                methodSpecs.add(
                        MethodSpec.methodBuilder(SET + formatName)
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addParameter(ClassName.get(enclosedElement.asType()),
                                        enclosedElement.getSimpleName().toString(), Modifier.FINAL)
                                .returns(TypeName.VOID)
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
                                .returns(TypeName.BOOLEAN)
                                .addJavadoc(String.format(REMOVE_METHOD_JAVA_DOC,
                                        enclosedElement.getSimpleName())
                                )
                                .build()
                );
            }
        }

        if (methodSpecs.size() > 0) {
            // The deleteAll method
            MethodSpec methodDelete = MethodSpec.methodBuilder(REMOVE_ALL)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(TypeName.BOOLEAN)
                    .addJavadoc(REMOVE_ALL_METHOD_JAVA_DOC)
                    .build();
            methodSpecs.add(methodDelete);
        }

        return TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addJavadoc(CLASS_JAVA_DOC)
                .build();
    }

    /**
     * Create AnnotationSpec
     *
     * @param element
     * @param typeName
     * @return
     */
    private AnnotationSpec createAnnotationSpec(Element element, String typeName) {
        if (typeName.equals("int")
                || typeName.equals("java.lang.Integer")) {
            DefInt defInt = element.getAnnotation(DefInt.class);
            return AnnotationSpec.builder(DefInt.class)
                    .addMember("value", "$L", defInt == null ? 0 : defInt.value())
                    .build();
        } else if (typeName.equals("java.lang.String")) {
            DefString defString = element.getAnnotation(DefString.class);
            return AnnotationSpec.builder(DefString.class)
                    .addMember("value", "$S", defString == null ? "" : defString.value())
                    .build();
        } else if (typeName.equals("boolean")
                || typeName.equals("java.lang.Boolean")) {
            DefBoolean defBoolean = element.getAnnotation(DefBoolean.class);
            return AnnotationSpec.builder(DefBoolean.class)
                    .addMember("value", "$L", defBoolean == null ? false : defBoolean.value())
                    .build();
        } else if (typeName.equals("long")
                || typeName.equals("java.lang.Long")) {
            DefLong defLong = element.getAnnotation(DefLong.class);
            return AnnotationSpec.builder(DefLong.class)
                    .addMember("value", "$LL", defLong == null ? 0 : defLong.value())
                    .build();
        } else if (typeName.equals("float")
                || typeName.equals("java.lang.Float")) {
            DefFloat defFloat = element.getAnnotation(DefFloat.class);
            return AnnotationSpec.builder(DefFloat.class)
                    .addMember("value", "$LF", defFloat == null ? 0 : defFloat.value())
                    .build();
        } else {
            return null;
        }
    }
}
