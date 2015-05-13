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

import android.net.LocalSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Yuriy Myronovych on 21/04/2015.
 */
public class LocalSocketServerHandler {
    private LocalSocket socket;
    private IResolver resolver;

    public LocalSocketServerHandler(IResolver resolver, LocalSocket socket) {
        this.socket = socket;
        this.resolver = resolver;
    }

    private boolean isStarted = false;
    public void handle() {
        if (isStarted) throw new RuntimeException("unexpected second call to handle");

        new Thread() {
            @Override
            public void run() {
                doHandle();
            }
        }.start();

        isStarted = true;
    }

    private void doHandle() {
        try {
            ObjectInputStream argsStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream resultStream = new ObjectOutputStream(socket.getOutputStream());
            String method = argsStream.readUTF();

            resolver.resolve(method, resultStream, argsStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch(Exception e){};
        }
    }
}
