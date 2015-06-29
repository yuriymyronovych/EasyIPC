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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * Created by Yuriy Myronovych on 06/05/2015.
 */
public class BaseClassGenerator {
    protected PersistanceResolver persistanceResolver = new PersistanceResolver();
    protected ProcessingEnvironment env;
    protected RoundEnvironment roundEnv;
    protected TypeElement classElem;
    protected PackageElement packageElement;

    protected Writer writer;

    Set<? extends Element> elementsListener;

    public BaseClassGenerator(ProcessingEnvironment env, RoundEnvironment roundEnv, TypeElement classElem) {
        this.env = env;
        this.roundEnv = roundEnv;
        this.classElem = classElem;
        this.packageElement = (PackageElement) classElem.getEnclosingElement();
        elementsListener = roundEnv.getElementsAnnotatedWith(EasyIPCListener.class);
    }

    public void finish() throws IOException {
        writer.write("\n}");
        writer.close();
        writer = null;
    }

    public String getUserClassName() {
        return classElem.getSimpleName().toString();
    }

    public String getFullUserClassName() {
        return packageElement.getQualifiedName().toString() + "." + getUserClassName();
    }

    protected boolean isEasyIPCListener(TypeMirror type) {
        for (Element e : elementsListener) {
            if (env.getTypeUtils().isSameType(e.asType(), type)) {
                return true;
            }
        }
        return false;
    }
}
