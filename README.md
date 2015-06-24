# EasyIPC
Custom Implementation of Android IPC.</br>

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
* Gradle
```Gradle
dependencies {
    compile "com.ym.easyipc:easyipc-api:1.+"
    provided "com.ym.easyipc:easyipc-api:1.+"
...
```
* Extend your service from EasyIPCService
* Mark the method in your service with @EasyIPCMethod
* In case you use any object as method args or retun param than make sure to mark it as Serializable
* build your project 
* Use auto-generated (*service_name* + Client).java to access your service
* Enjoy xD

# Q&A
1. How can a remote app access my service? </br>
Just copy (*service_name* + Client).java to the client app and use it.
2. My IDE show a compilation error and can't find a (*service_name* + Client).java! </br>
The Client class is generated during compilation at the path build/intermediates/classes/release/. You can add that folder as a source to your project or just ignore the compilation error from IDE. Gradle should build it fine.
