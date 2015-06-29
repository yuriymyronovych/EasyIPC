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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;

/**
 * Created by Yuriy Myronovych on 26/06/2015.
 */
public class ListenerGenerator extends BaseClassGenerator {
    public ListenerGenerator(ProcessingEnvironment env, RoundEnvironment roundEnv, TypeElement classElem) {
        super(env, roundEnv, classElem);
    }

    public void generateClass() throws IOException {
        env.getMessager().printMessage(Diagnostic.Kind.NOTE, ">>generating class: " + getClassName());
        writer = env.getFiler().createSourceFile(getClassName()).openWriter();
        writer.write("package " + packageElement.getQualifiedName() + ";\n\n");
        writer.write(String.format(Constants.LISTENER_STATIC_CONTENT, getFullUserClassName(), getClassName()));
        writer.close();
        env.getMessager().printMessage(Diagnostic.Kind.NOTE, "<<finished with class: " + getClassName());
    }

    public String getClassName() {
        return classElem.getSimpleName() + Constants.LISTENER_IMPL_TAG;
    }
}
