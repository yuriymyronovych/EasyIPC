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

/**
 * Created by Yuriy Myronovych on 28/04/2015.
 */
public class Constants {
    public static final String CLASS_RESOLVER_NAME_TAG = "$EasyIPC";
    public static final String CLASS_CLIENT_NAME_TAG = "Client";
    public static final String RESOLVER_INTERFACE = "com.ym.easyipc_api.lib.IResolver";
    public static final String EasyIPCService = "com.ym.easyipc_api.lib.EasyIPCService";
    public static final String RESOLVE_METHOD_DECLARATION = "public void resolve(String method, java.io.ObjectOutputStream resultStream, java.io.ObjectInputStream argsStream) throws java.lang.Exception";

    public static final String ARGS_STREAM = "argsStream";
    public static final String RES_STREAM = "resultStream";

    public static final String CLIENT_STATIC_CONTENT = "" +
        "public class %2$s extends com.ym.easyipc_api.lib.BaseClient {\n" +
        "\tprivate %2$s() {}\n" +
        "\tpublic static void bind(android.content.Context ctx, final ServiceConnection conn, int flags) {\n" +
        "\t\tctx.bindService(new android.content.Intent(ctx, %1$s.class), new android.content.ServiceConnection() {\n" +
        "\t\t\t@Override\n" +
        "\t\t\tpublic void onServiceConnected(android.content.ComponentName componentName, android.os.IBinder iBinder) {\n" +
        "\t\t\t\tconn.onServiceConnected(new %2$s());\n" +
        "\t\t\t}\n" +
        "\n" +
        "\t\t\t@Override\n" +
        "\t\t\tpublic void onServiceDisconnected(android.content.ComponentName componentName) {\n" +
        "\t\t\t\tconn.onServiceDisconnected();\n" +
        "\t\t\t}\n" +
        "\t\t}, flags);\n" +
        "\t}\n" +
        "\n" +
        "\tpublic interface ServiceConnection {\n" +
        "\tvoid onServiceConnected(%2$s client);\n" +
        "\n" +
        "\tvoid onServiceDisconnected();\n" +
        "\t}\n" +
        "\t@Override\n" +
        "\tprotected String getAddress() {\n" +
        "\t\treturn \"%3$s\";\n" +
        "\t}\n" +
        "//--------------------------------------------------------------------------------------------------------------------------------------------\n" +
        "\n";
}
