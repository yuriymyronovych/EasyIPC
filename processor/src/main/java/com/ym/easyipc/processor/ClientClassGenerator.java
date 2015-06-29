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

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Yuriy Myronovych on 06/05/2015.
 */
public class ClientClassGenerator extends BaseClassGenerator {

    public ClientClassGenerator(ProcessingEnvironment env, RoundEnvironment roundEnv, TypeElement classElem) {
        super(env, roundEnv, classElem);
    }

    public void generateClassDeclaration() throws Exception {
        writer = env.getFiler().createSourceFile(getClassName()).openWriter();
        writer.write("package " + packageElement.getQualifiedName() + ";\n\n");
        String extendsMod = "";
        if (isEasyIPCListener(classElem.asType())) {
            extendsMod += " implements " + getFullUserClassName() + ", java.io.Serializable";
        }
        writer.write(String.format(Constants.CLIENT_STATIC_CONTENT, getUserClassName(), getClassName(), getFullUserClassName(), extendsMod));
    }

    public void generateMethods(List<ExecutableElement> methodElems) throws IOException {
        for (ExecutableElement method : methodElems) {
            writer.write("public " + method.getReturnType() + " " + method.getSimpleName() + " (");
            //method args
            for (int i = 0; i < method.getParameters().size(); i++) {
                if (i != 0) {
                    writer.write(", ");
                }
                VariableElement var = method.getParameters().get(i);
                if (isEasyIPCListener(var.asType())) {
                    writer.write(var.asType() + Constants.LISTENER_IMPL_TAG + " arg" + i);
                } else {
                    writer.write(var.asType() + " arg" + i);
                }
            }
            writer.write(") {\n");

            //method body
            boolean isVoidResult = method.getReturnType().toString().equals("void");
            if (!isVoidResult) {
                writer.write(method.getReturnType() + " result = " + Utils.getDefaultValue(method.getReturnType().toString()) + ";\n");
            }
            writer.write("try {\n");
            writer.write("SocketClient client = baseClient.connect();\n");
            writer.write("client.argsStream.writeUTF(\"" + Utils.toMethodId(method) + "\");\n");
            for (int i = 0; i < method.getParameters().size(); i++) {
                VariableElement var = method.getParameters().get(i);
                if (isEasyIPCListener(var.asType())) {
                    writer.write("client.argsStream.writeUTF(arg" + i + ".getGuid());\n");
                } else {
                    String dataSign = persistanceResolver.resolveObjectStreamDataSign(var.asType().toString());
                    writer.write("client.argsStream.write" + dataSign + "(arg" + i + ");\n");
                }
            }
            writer.write("client.argsStream.flush();\n");
            if (!isVoidResult) {
                String dataSign = persistanceResolver.resolveObjectStreamDataSign(method.getReturnType().toString());
                writer.write("result = (" + method.getReturnType() + ")");
                writer.write("client.resStream.read" + dataSign + "();\n");
            }
            writer.write("client.resStream.readInt();\n");
            writer.write("client.finish();\n");
            writer.write("} catch (Exception e) {\n");
            writer.write("e.printStackTrace();\n");
            writer.write("}\n");
            if (!isVoidResult) {
                writer.write("return result;\n");
            }

            writer.write("}\n\n");
        }
    }



    public String getClassName() {
        return classElem.getSimpleName() + Constants.CLASS_CLIENT_NAME_TAG;
    }
}
