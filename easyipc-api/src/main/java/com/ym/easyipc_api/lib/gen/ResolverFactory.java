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

/**
 * Created by Yuriy Myronovych on 21/04/2015.
 */
public class ResolverFactory {
    public static IResolver getResolver(EasyIPCService service) {
        try {
            Class c = Class.forName("com.ym.easyipc_api.lib.gen." + service.getClass().getSimpleName() + Constants.CLASS_RESOLVER_NAME_TAG);
            IResolver resolver = (IResolver) c.newInstance();
            resolver.setService(service);
            return resolver;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
