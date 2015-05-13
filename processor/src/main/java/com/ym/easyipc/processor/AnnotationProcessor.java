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

import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Yuriy Myronovych on 21/04/2015.
 */
@SupportedAnnotationTypes("com.ym.easyipc.processor.EasyIPCMethod")
public class AnnotationProcessor extends AbstractProcessor {

    private HashMap<TypeElement, List<ExecutableElement>> jobs;
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        jobs = new HashMap<TypeElement, List<ExecutableElement>>();
        try {
            for (Element elem : roundEnv.getElementsAnnotatedWith(EasyIPCMethod.class)) {
                if (elem.getKind() != ElementKind.METHOD) throw new Exception("only methods can be marked by EasyIPCMethod");
                ExecutableElement methodElem = (ExecutableElement) elem;
                TypeElement classElem = (TypeElement) methodElem.getEnclosingElement();
//                if (!hasEasyIPCServiceParent(methodClass)) throw new Exception("class should extend EasyIPCService");

                if (jobs.get(classElem) == null) {
                    jobs.put(classElem, new ArrayList<ExecutableElement>());
                }
                jobs.get(classElem).add(methodElem);

                String message = "Method: " + methodElem.getSimpleName() + " / " + methodElem.getEnclosingElement().getSimpleName() + ":\n";
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
            }

            if (!jobs.isEmpty()) {
                new Generator(processingEnv, jobs).generate();
                return true;
            }
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "failed to generate EasyIPC classes:\n " + ExceptionUtils.getFullStackTrace(e));

        }
        return false;
    }

    //TODO not working as EasyIPCService is not part of this package
//    private boolean hasEasyIPCServiceParent(TypeElement methodClass) {
//        return processingEnv.getTypeUtils().isAssignable(methodClass.asType(), processingEnv.getElementUtils().getTypeElement("com.ym.easyipc_api.lib.EasyIPCService").asType());
//    }
}
