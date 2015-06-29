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

import android.net.LocalServerSocket;
import com.ym.easyipc_api.lib.gen.ResolverFactory;

import java.io.IOException;

/**
 * Created by Yuriy Myronovych on 25/06/2015.
 */
public class EasyIPCServerHandler {
    protected IResolver resolver;
    protected HandlerThread handlerThread;
    protected String address;

    public EasyIPCServerHandler(Object target) {
        resolver = ResolverFactory.getResolver(target);
        handlerThread = new HandlerThread();
        address = resolver.getAddress();
    }

    public EasyIPCServerHandler(Object target, String address) {
        resolver = ResolverFactory.getResolver(target);
        handlerThread = new HandlerThread();
        this.address = address;
    }

    public void start() {
        handlerThread.start();
    }

    public void stop() {
        handlerThread.interrupt();
    }

    class HandlerThread extends Thread {
        @Override
        public void run() {
            try {
                LocalServerSocket socket = new LocalServerSocket(address);
                while (!isInterrupted()) {
                    new LocalSocketServerHandler(resolver, socket.accept()).handle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("EasyIPCServerHandler stoped");
        }
    }
}
