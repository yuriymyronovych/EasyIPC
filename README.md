# EasyIPC
Custom Implementation of android IPC.</br>
Note: waiting for jcenter to approve 1.0.3

* forget aidl
* forget parcelable
* forget 10MB limit
* just add @EasyIPCMethod annotation and you are done 

```java
//This is your service
public class TestService extends EasyIPCService {

    @EasyIPCMethod
    public String test(String s, int a, MySerialisableObject myObj) {
        return s + a + myObj.toString();
    }
}

//the TestServiceClient.java is generated for you
TestServiceClient.bind(this, new TestServiceClient.ServiceConnection() {
            @Override
            public void onServiceConnected(TestServiceClient client) {
                System.out.println(client.test("", 1, new MySerialisableObject());
            }

            @Override
            public void onServiceDisconnected() {}
        }, Context.BIND_AUTO_CREATE);
```
# Integration Guide
1. Gradle
```
dependencies {
        classpath 'com.android.tools.build:gradle:1.1.3'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

repositories {
    jcenter()
}

dependencies {
    apt "com.ym.easyipc:easyipc-api:1.+"
    compile "com.ym.easyipc:easyipc-api:1.+"
...
```
2. Extend your service from EasyIPCService
3. Mark the method in your service with @EasyIPCMethod
4. In case you use any object as method args or retun param than make sure to mark it as Serializable
5. build your project 
6. Use auto-generated (*service_name* + Client).java to access your service
7. Enjoy xD
