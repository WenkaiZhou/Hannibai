package com.kevin.hannibai.compiler;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Created by zhouwenkai on 2017/8/12.
 */

class Utils {

    public static boolean isSubtype(Element typeElement, String type) {
        return EnvironmentManager.getManager().getTypeUtils().isSubtype(typeElement.asType(),
                EnvironmentManager.getManager().getElementUtils().getTypeElement(type).asType());
    }

    public static String capitalize(CharSequence self) {
        return self.length() == 0 ? "" :
                "" + Character.toUpperCase(self.charAt(0)) + self.subSequence(1, self.length());
    }

    public static void note(String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        EnvironmentManager.getManager().getMessager().printMessage(Diagnostic.Kind.NOTE, message, null);
    }

    public static void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        EnvironmentManager.getManager().getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}