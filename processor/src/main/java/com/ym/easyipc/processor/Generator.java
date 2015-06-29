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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yuriy Myronovych on 28/04/2015.
 */
public class Generator {
    private HashMap<TypeElement, List<ExecutableElement>> jobs;
    private ProcessingEnvironment env;
    private RoundEnvironment roundEnv;

    public Generator(ProcessingEnvironment env, RoundEnvironment roundEnv, HashMap<TypeElement, List<ExecutableElement>> jobs) {
        this.env = env;
        this.jobs = jobs;
        this.roundEnv = roundEnv;
    }

    public void generate() throws Exception {
        for (TypeElement classElem : jobs.keySet()) {
            ResolverClassGenerator classGen = new ResolverClassGenerator(env, roundEnv, classElem);
            env.getMessager().printMessage(Diagnostic.Kind.NOTE, ">>generating class: " + classGen.getClassName());
            
            classGen.generateClassDeclaration();
            classGen.generateResolveMethod(jobs.get(classElem));
            classGen.finish();

            ClientClassGenerator clientClassGen = new ClientClassGenerator(env, roundEnv, classElem);
            env.getMessager().printMessage(Diagnostic.Kind.NOTE, "<<finished with class: " + classGen.getClassName());
            clientClassGen.generateClassDeclaration();
            clientClassGen.generateMethods(jobs.get(classElem));
            clientClassGen.finish();
        }

    }
}
