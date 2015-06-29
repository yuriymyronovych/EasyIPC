package com.ym.easyipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.ym.easyipc.IListenerImpl;
import com.ym.easyipc.TestServiceClient;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by yum01 on 22/04/2015.
 */
public class Activity1 extends Activity {
    private TestServiceClient serviceClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("bind to service");
        bindService(new Intent(this, TestService.class), new TestServiceClient.Connection() {
            @Override
            public void onClientConnected(TestServiceClient client) {
                System.out.println("service connected");
                serviceClient = client;
                System.out.println(serviceClient.test(1, 1f, 1d, Byte.valueOf("1"), false, 's', 1L, "", Collections.<Integer>emptyList()));
                System.out.println(Arrays.toString(serviceClient.testArray(new int[]{1, 2, 3})));

                testListener(serviceClient);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceClient = null;
            }
        }, Context.BIND_AUTO_CREATE);

    }

    private void testListener(final TestServiceClient serviceClient) {
        final IListenerImpl listener = new IListenerImpl() {
            @Override
            public void onResult(int result) {
                System.out.println("listener received: " + result);
            }
        };
        serviceClient.testAddListener(listener);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                serviceClient.testRemoveListener(listener);
                listener.release();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceClient != null) {
            unbindService(serviceClient.getConnection());
        }
    }
}
