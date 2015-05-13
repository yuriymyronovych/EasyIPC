/*
 * Copyright 2015 Yuriy Myronovych
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ym.easyipc.processor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by Yuriy Myronovych on 05/05/2015.
 */
public class Utils {
    public static String toMethodId(ExecutableElement method) {
        String s = method.getSimpleName().toString() + "$";
        for (VariableElement var : method.getParameters()) {
            s += var.asType().toString() + "$";
        }
        return s;
    }

    public static String fromMethodId(String methodId) {
        return methodId.split("$")[0];
    }

    public static String getDefaultValue(String type) {
        if (type.equals("byte")) {
            return "0";
        } else if (type.equals("boolean")) {
            return "false";
        } else if (type.equals("char")) {
            return "''";
        } else if (type.equals("double")) {
            return "0";
        } else if (type.equals("float")) {
            return "0";
        } else if (type.equals("int")) {
            return "0";
        } else if (type.equals("long")) {
            return "0";
        } else if (type.equals("short")) {
            return "0";
        } else if (type.equals("String")) {
            return "\"\"";
        } else {
            return "null";
        }
    }
}
