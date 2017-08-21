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