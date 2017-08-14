package com.kevin.hannibai.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.kevin.hannibai.compiler.Constants.CLASS_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.GET;
import static com.kevin.hannibai.compiler.Constants.GET_METHOD_JAVA_DOC;
import static com.kevin.hannibai.compiler.Constants.HANNIBAI;
import static com.kevin.hannibai.compiler.Constants.PACKAGE_NAME;
import static com.kevin.hannibai.compiler.Constants.REAL_HANNIBAI;

/**
 * Created by zhouwenkai on 2017/8/13.
 */

public class TableGenerator implements Generator {

    private final Set<TypeElement> mElements;

    public TableGenerator(Set<TypeElement> elements) {
        this.mElements = elements;

    }

    @Override
    public void generate() {
        HashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();

        MethodSpec initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.VOID)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addStatement("$T.getInstance().init(context)", ClassName.get(PACKAGE_NAME, REAL_HANNIBAI))
                .build();

        methodSpecs.add(initMethod);

        for (TypeElement mElement : mElements) {
            String packageName = mElement.getEnclosingElement().toString();
            String className = mElement.getSimpleName().toString();
            String returnClassName = mElement.getSimpleName().toString() + HANNIBAI;

//            MethodSpec methodSpec = MethodSpec.methodBuilder(GET + Utils.capitalize(className))
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(ClassName.get(packageName, returnClassName))
//                    .beginControlFlow("try")
//                    .addStatement("Class<?> clazz = Class.forName(\"com.kevin.hannibai.Hannibai\")")
//                    .addStatement("$T hannibai = ($T) clazz.getMethod(\"getInstance\").invoke(null)", ClassName.get("com.kevin.hannibai", "Hannibai"), ClassName.get("com.kevin.hannibai", "Hannibai"))
//                    .addStatement("$T create = clazz.getMethod(\"create\", Class.class)", ClassName.get("java.lang.reflect", "Method"))
//                    .addStatement("return ($T) create.invoke(hannibai, $T.class)", ClassName.get(packageName, returnClassName), ClassName.get(packageName, returnClassName))
//                    .endControlFlow()
//                    .beginControlFlow("catch ($T e) ", Exception.class)
//                    .addStatement("e.printStackTrace()")
//                    .addStatement("return null")
//                    .endControlFlow()
//                    .build();

            MethodSpec methodSpec = MethodSpec.methodBuilder(GET + Utils.capitalize(className))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(ClassName.get(packageName, returnClassName))
                    .addStatement("return $T.getInstance().create($T.class)", ClassName.get(PACKAGE_NAME, REAL_HANNIBAI), ClassName.get(packageName, returnClassName))
                    .build();

            methodSpecs.add(methodSpec);
        }

        TypeSpec typeSpec = TypeSpec.classBuilder("HannibaiTable")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .addJavadoc(CLASS_JAVA_DOC)
                .build();

        try {
            JavaFile.builder("com.kevin.hannibai", typeSpec)
                    .build()
                    .writeTo(EnvironmentManager.getManager().getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
