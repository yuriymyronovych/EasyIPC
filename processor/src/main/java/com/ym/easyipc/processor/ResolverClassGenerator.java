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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by Yuriy Myronovych on 28/04/2015.
 */
public class ResolverClassGenerator extends BaseClassGenerator {


    public ResolverClassGenerator(ProcessingEnvironment env, RoundEnvironment roundEnv, TypeElement classElem) {
        super(env, roundEnv, classElem);
    }

    public void generateClassDeclaration() throws Exception {
        writer = env.getFiler().createSourceFile(getClassName()).openWriter();
        writer.write("package " + "com.ym.easyipc_api.lib.gen" + ";\n\n");
        writer.write("class " + getClassName() + " implements " + Constants.RESOLVER_INTERFACE + " {\n\n");
        //write default methods
        writer.write("\tprivate " + getFullUserClassName() + " target;\n\n");
//        writer.write("\tpublic " + getClassName() + "(){}\n\n");

        writer.write("\tpublic void setTarget(Object target) {\n");
        writer.write("\t\tthis.target = (" + getFullUserClassName() + ") target;\n\t}\n\n");

        writer.write("\t public String getAddress() {\n");
        writer.write("\t\t return \"" + getFullUserClassName() + "\";\n\t}\n\n");
    }

    public void generateResolveMethod(List<ExecutableElement> methodElems) throws IOException {
        writer.write("\t" + Constants.RESOLVE_METHOD_DECLARATION + "{\n");
        boolean isFirst = true;
        for (ExecutableElement method : methodElems) {
            env.getMessager().printMessage(Diagnostic.Kind.NOTE, "generating EasyIPCMethod: " + method.getSimpleName());
            if (!isFirst) {
                writer.write(" else ");
            } else {
                isFirst = false;
            }
            writer.write("\t\tif (method.equals(\"" + Utils.toMethodId(method) + "\")){\n");
            generateReadArgs(method);
            generateServiceCallAndWriteResult(method);
            writer.write("\n\t\t}");
        }
        writer.write("\n\t}");
    }

    private void generateReadArgs(ExecutableElement method) throws IOException {
        int i = 0;
        for (VariableElement var : method.getParameters()) {
            String varType = var.asType().toString();

            if (isEasyIPCListener(var.asType())) {
                writer.write("\t\t\tString guid" + i + " = " + Constants.ARGS_STREAM + ".readUTF();\n");
                String listenerImpl = varType + Constants.LISTENER_IMPL_TAG;
                writer.write(String.format(Constants.LISTENER_STATIC_POSTPROCESS, "guid" + i, "listenerarg" + i, varType + Constants.CLASS_CLIENT_NAME_TAG));
            } else {
                String type = persistanceResolver.resolveObjectStreamDataSign(varType);
                writer.write("\t\t\t" + varType + " arg" + i + " = ");
                if (type.equals("Object")) writer.write("(" + varType + ")"); //type conversation
                writer.write(Constants.ARGS_STREAM + ".read" + type + "();\n");
            }

            i++;
        }
    }

    private void generateServiceCallAndWriteResult(ExecutableElement method) throws IOException {
        String returnType = method.getReturnType().toString();
        if (!returnType.equals("void")) {
            writer.write("\t\t\t" + returnType + " result = ");
        }
        writer.write("target." + method.getSimpleName() + "(");
        for (int i = 0; i < method.getParameters().size(); i++) {
            if (i != 0) writer.write(", ");
            if (isEasyIPCListener(method.getParameters().get(i).asType())) {
                writer.write("listenerarg" + i);
            } else {
                writer.write("arg" + i);
            }
        }
        writer.write(");\n");

        if (!returnType.equals("void")) {
            String returnTypeSign = persistanceResolver.resolveObjectStreamDataSign(returnType);
            writer.write("\t\t\t" + Constants.RES_STREAM + ".write" + returnTypeSign + "(result);\n");
        }
        writer.write("\t\t\t" + Constants.RES_STREAM + ".writeInt(1);\n");
        writer.write("\t\t\t" + Constants.RES_STREAM + ".flush();");
    }

    public String getClassName() {
        return classElem.getSimpleName() + Constants.CLASS_RESOLVER_NAME_TAG;
    }


}
