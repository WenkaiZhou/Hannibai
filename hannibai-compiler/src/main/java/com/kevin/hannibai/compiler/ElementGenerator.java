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
