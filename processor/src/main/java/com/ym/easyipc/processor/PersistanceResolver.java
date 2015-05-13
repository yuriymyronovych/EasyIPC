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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Yuriy Myronovych on 05/05/2015.
 */
public class PersistanceResolver {

    public String resolveObjectStreamDataSign(String type) throws IOException {
        if (type.equals("byte")) {
            return "Byte";
        } else if (type.equals("boolean")) {
            return "Boolean";
        } else if (type.equals("char")) {
            return "Char";
        } else if (type.equals("double")) {
            return "Double";
        } else if (type.equals("float")) {
            return "Float";
        } else if (type.equals("int")) {
            return "Int";
        } else if (type.equals("long")) {
            return "Long";
        } else if (type.equals("short")) {
            return "Short";
        } else if (type.equals("String")) {
            return "UTF";
        } else {
            return "Object";
        }
    }
}
