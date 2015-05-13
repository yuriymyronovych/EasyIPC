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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yuriy Myronovych on 06/05/2015.
 */
public class ClientClassGenerator extends BaseClassGenerator {

    public ClientClassGenerator(ProcessingEnvironment env, TypeElement classElem) {
        super(env, classElem);
    }

    public void generateClassDeclaration() throws Exception {
        writer = env.getFiler().createSourceFile(getClassName()).openWriter();
        writer.write("package " + packageElement.getQualifiedName() + ";\n\n");
        writer.write(String.format(Constants.CLIENT_STATIC_CONTENT, getUserClassName(), getClassName(), getFullUserClassName()));
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
                writer.write(var.asType() + " arg" + i);
            }
            writer.write(") {\n");

            //method body
            writer.write(method.getReturnType() + " result = " + Utils.getDefaultValue(method.getReturnType().toString()) + ";\n");
            writer.write("try {\n");
            writer.write("SocketClient client = new SocketClient().connect();\n");
            writer.write("client.argsStream.writeUTF(\"" + Utils.toMethodId(method) + "\");\n");
            for (int i = 0; i < method.getParameters().size(); i++) {
                VariableElement var = method.getParameters().get(i);
                String dataSign = persistanceResolver.resolveObjectStreamDataSign(var.asType().toString());
                writer.write("client.argsStream.write" + dataSign + "(arg" + i + ");\n");
            }
            writer.write("client.argsStream.flush();\n");
            String dataSign = persistanceResolver.resolveObjectStreamDataSign(method.getReturnType().toString());
            writer.write("result = (" + method.getReturnType() + ")");
            writer.write("client.resStream.read" + dataSign + "();\n");
            writer.write("client.finish();\n");
            writer.write("} catch (Exception e) {\n");
            writer.write("e.printStackTrace();\n");
            writer.write("}\n");
            writer.write("return result;\n");


            writer.write("}\n\n");
        }
    }

    public String getClassName() {
        return classElem.getSimpleName() + "Client";
    }
}
