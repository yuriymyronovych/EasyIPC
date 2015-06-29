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

import com.ym.easyipc_api.lib.BaseClient.SocketClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Yuriy Myronovych on 25/06/2015.
 */
public class EasyIPCListenerWrapper implements Serializable {
    static final long serialVersionUID = 1L;
    private String guid;
    private transient Object clientListener;
    private transient SocketClient socketClient;
    private transient EasyIPCServerHandler server;

    public EasyIPCListenerWrapper(Object clientListener) {
        this.clientListener = clientListener;
        guid = UUID.randomUUID().toString();
    }

    public void startServer() {
        server = new EasyIPCServerHandler(clientListener, guid);
        server.start();
    }

    public void stopServer() {
        server.stop();
    }

    public void connectClient() throws IOException {
        socketClient = new BaseClient(guid).connect();
    }

    public void disconnectClient() throws IOException {
        socketClient.finish();
    }
}
