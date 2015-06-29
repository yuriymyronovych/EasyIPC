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
    public static final String LISTENER_IMPL_TAG = "Impl";
    public static final String RESOLVER_INTERFACE = "com.ym.easyipc_api.lib.IResolver";
    public static final String EasyIPCService = "com.ym.easyipc_api.lib.EasyIPCService";
    public static final String RESOLVE_METHOD_DECLARATION = "public void resolve(String method, java.io.ObjectOutputStream resultStream, java.io.ObjectInputStream argsStream) throws java.lang.Exception";

    public static final String ARGS_STREAM = "argsStream";
    public static final String RES_STREAM = "resultStream";

    public static final String CLIENT_STATIC_CONTENT = "" +
            "import android.os.IBinder; \n" +
            "import android.content.ServiceConnection; \n" +
            "import android.content.ComponentName; \n" +
            "import com.ym.easyipc_api.lib.BaseClient; \n" +
            "import com.ym.easyipc_api.lib.BaseClient.SocketClient;\n\n" +
            "public class %2$s %4$s {\n" +
            "\tprivate BaseClient baseClient;\n" +
            "\tprivate Connection connection;\n" +
            "\tpublic %2$s(Connection c, String address) {\n" +
            "\t\tconnection = c;\n" +
            "\t\tbaseClient = new BaseClient(address);\n" +
            "\t}\n\n" +
            "\tpublic Connection getConnection() {\n" +
            "\t\treturn connection;\n" +
            "\t}\n\n" +

            "\n" +
            "\tpublic static abstract class Connection implements ServiceConnection {\n" +
            "\t@Override\n" +
            "\tpublic void onServiceConnected(ComponentName componentName, IBinder iBinder) {\n" +
            "\t\tonClientConnected(new %2$s(this, \"%3$s\"));\n" +
            "\t}\n\n" +
            "\tpublic abstract void onClientConnected(%2$s client);\n" +
            "\t}\n" +
            "//--------------------------------------------------------------------------------------------------------------------------------------------\n" +
            "\n";

    public static final String LISTENER_STATIC_CONTENT = "" +
            "import com.ym.easyipc_api.lib.EasyIPCServerHandler;\n\n" +
            "public abstract class %2$s implements %1$s, com.ym.easyipc_api.lib.GuidHolder, java.io.Serializable {\n" +
            "static final long serialVersionUID = 1L;\n" +
            "private String guid;\n" +
            "private transient EasyIPCServerHandler server;\n\n" +
            "public %2$s() {\n" +
            "\tthis.guid = java.util.UUID.randomUUID().toString();\n" +
            "\tserver = new EasyIPCServerHandler(this, guid);\n" +
            "\tserver.start();\n" +
            "}\n\n" +
            "public void release() {\n" +
            "\tserver.stop();\n" +
            "}\n\n" +
            "public String getGuid() {\n" +
            "\treturn guid;\n" +
            "}\n\n" +
            "}";

    public static final String LISTENER_STATIC_POSTPROCESS = "" +
            "\t\t\t%3$s %2$s = com.ym.easyipc_api.lib.ListenersRegistry.getInstance().getListener(%1$s);\n" +
            "\t\t\tif (%2$s == null) {\n" +
            "\t\t\t\t%2$s = new %3$s(null, %1$s);\n" +
            "\t\t\t\tcom.ym.easyipc_api.lib.ListenersRegistry.getInstance().put(%1$s, %2$s);\n" +
            "\t\t\t}\n";
}
