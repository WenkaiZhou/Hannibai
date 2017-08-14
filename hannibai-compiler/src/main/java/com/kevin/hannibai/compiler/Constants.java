package com.kevin.hannibai.compiler;

/**
 * Created by zhouwenkai on 2017/8/12.
 */

public class Constants {
    public static final String CLASS_JAVA_DOC = "Generated by Hannibai. Do not edit it!\n";

    public static final String GET = "get";
    public static final String SET = "set";
    public static final String REMOVE = "remove";
    public static final String REMOVE_ALL = "removeAll";

    public static final String PACKAGE_NAME = "com.kevin.hannibai";
    public static final String DOT = ".";
    public static final String HANNIBAI = "Hannibai";
    public static final String REAL_HANNIBAI = "RealHannibai";

    public static final String GET_METHOD_JAVA_DOC = "Retrieve the %s from the preferences. \n\n@return Returns the preference value if it exists, or defValue.\n";
    public static final String PUT_METHOD_JAVA_DOC = "Set the %s value to the preferences. \n\n@param %s The new value for the preference.\n";
    public static final String REMOVE_METHOD_JAVA_DOC = "Remove the %s in the preferences. \n\n@return Whether remove success.\n";
    public static final String REMOVE_ALL_METHOD_JAVA_DOC = "Remove all preferences in the preferences. \n\n@return Whether remove success.\n";




    public static final String HANNIBAI_ANNOTATION_TYPE = "com.kevin.hannibai.annotation.SharePreference";
}
