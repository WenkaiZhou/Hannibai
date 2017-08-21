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

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by zhouwenkai on 2017/8/12.
 */

class EnvironmentManager {

    // 处理Element的工具类
    private Elements elementUtils;
    // 使用Filer你可以创建文件
    private Filer filer;
    // 日志相关的辅助类
    private Messager messager;
    // 处理TypeMirror的工具类
    private Types typeUtils;

    private EnvironmentManager() {
    }

    public static EnvironmentManager getManager() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final EnvironmentManager INSTANCE = new EnvironmentManager();
    }

    public void init(ProcessingEnvironment environment) {
        setTypeUtils(environment.getTypeUtils());
        setElementUtils(environment.getElementUtils());
        setFiler(environment.getFiler());
        setMessager(environment.getMessager());
    }

    public Elements getElementUtils() {
        return elementUtils;
    }

    public void setElementUtils(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    public Filer getFiler() {
        return filer;
    }

    public void setFiler(Filer filer) {
        this.filer = filer;
    }

    public Messager getMessager() {
        return messager;
    }

    public void setMessager(Messager messager) {
        this.messager = messager;
    }

    public Types getTypeUtils() {
        return typeUtils;
    }

    public void setTypeUtils(Types typeUtils) {
        this.typeUtils = typeUtils;
    }
}

