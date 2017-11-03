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

import com.google.auto.service.AutoService;
import com.kevin.hannibai.annotation.SharePreference;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.kevin.hannibai.compiler.Constants.HANDLE_IMPL_SUFFIX;
import static com.kevin.hannibai.compiler.Constants.HANDLE_SUFFIX;
import static com.kevin.hannibai.compiler.Constants.HANNIBAI_ANNOTATION_TYPE;

/**
 * Created by zhouwenkai on 2017/8/12.
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes(HANNIBAI_ANNOTATION_TYPE)
public class HannibaiProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        EnvironmentManager.getManager().init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(SharePreference.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }
        // 合法的TypeElement集合
        Set<TypeElement> typeElements = new HashSet<>();
        for (Element element : elements) {
            if (validateElement(element)) {
                typeElements.add((TypeElement) element);
            }
        }

        if (typeElements.size() > 0) {
            new IHandleGenerator().generate();

            for (TypeElement typeElement : typeElements) {
                Utils.note("Hannibai process " + typeElement.getSimpleName());
                HandleGenerator handleGenerator = new HandleGenerator(typeElement, HANDLE_SUFFIX);
                handleGenerator.generate();
                new HandleImplGenerator(typeElement, HANDLE_IMPL_SUFFIX, handleGenerator.mClassName).generate();
            }
        }

        return true;
    }

    /**
     * Verify the annotated class.
     */
    private boolean validateElement(Element element) {
        // Element是不是类，规定SharePreference只能注解到类上面
        if (element.getKind() != ElementKind.CLASS) {
            Utils.error(element, "Only classes can be annotated with @%s.", SharePreference.class.getSimpleName());
            return false;
        }

        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE)) {
            // 类是不是private的
            Utils.error(element, "The class %s should not be modified by private.", element.getSimpleName());
            return false;
        } else if (modifiers.contains(Modifier.ABSTRACT)) {
            // 类是不是abstract的
            Utils.error(element, "The class %s should not be modified by abstract.", element.getSimpleName());
            return false;
        }

        return true;
    }

}
