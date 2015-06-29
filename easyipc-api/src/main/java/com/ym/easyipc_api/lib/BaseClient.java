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
import android.net.LocalSocketAddress;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Yuriy Myronovych on 12/05/2015.
 */
public class BaseClient {
    protected String address;

    public BaseClient(String address) {
        this.address = address;
    }

    public SocketClient connect() throws IOException {
        return new SocketClient().connect();
    }


    public class SocketClient {
        protected LocalSocket socket;
        public ObjectInputStream resStream;
        public ObjectOutputStream argsStream;

        public SocketClient connect() throws IOException {
            socket = new LocalSocket();
            socket.connect(new LocalSocketAddress(address));
            argsStream = new ObjectOutputStream(socket.getOutputStream());
            resStream = new ObjectInputStream(socket.getInputStream());
            return this;
        }

        public void finish() throws IOException {
            socket.close();
        }
    }
}
