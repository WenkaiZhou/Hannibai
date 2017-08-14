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

