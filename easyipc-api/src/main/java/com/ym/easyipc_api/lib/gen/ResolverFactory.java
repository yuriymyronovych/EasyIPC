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

package com.ym.easyipc_api.lib.gen;

import com.ym.easyipc.processor.Constants;
import com.ym.easyipc_api.lib.EasyIPCService;
import com.ym.easyipc_api.lib.IResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yuriy Myronovych on 21/04/2015.
 */
public class ResolverFactory {
    public static IResolver getResolver(Object target) {
        try {
            Class c = getResolverClass(target.getClass());
            IResolver resolver = (IResolver) c.newInstance();
            resolver.setTarget(target);
            return resolver;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Class getResolverClass(Class targetClass) {
        Class result = null;

        List<Class> options = new ArrayList<Class>();
        while (targetClass != Object.class) {
            options.add(targetClass);
            options.addAll(Arrays.asList(targetClass.getInterfaces()));
            targetClass = targetClass.getSuperclass();
        }

        for (Class clazz : options) {
            try {
                String className = "com.ym.easyipc_api.lib.gen." + clazz.getSimpleName() + Constants.CLASS_RESOLVER_NAME_TAG;
                result = Class.forName(className);
                break;
            } catch (ClassNotFoundException e) {}
        }

        return result;
    }
}
