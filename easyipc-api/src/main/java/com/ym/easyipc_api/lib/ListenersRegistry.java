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

package com.ym.easyipc_api.lib;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Yuriy Myronovych on 26/06/2015.
 */
public class ListenersRegistry {
    private static ListenersRegistry instance;

    public static synchronized ListenersRegistry getInstance() {
        if (instance == null) {
            instance = new ListenersRegistry();
        }
        return instance;
    }

    private HashMap<String, WeakReference<Object>> listeners = new HashMap<String, WeakReference<Object>>();

    public <T> T getListener(String guid) {
        WeakReference<Object> listener = listeners.get(guid);
        return listener != null ? (T) listener.get() : null;
    }

    public void put(String guid, Object listener) {
        if (listeners.containsKey(guid)) throw new RuntimeException("WTF?");
        listeners.put(guid, new WeakReference<Object>(listener));
    }
}
