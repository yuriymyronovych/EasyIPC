package com.ym.easyipc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.ym.easyipc.TestServiceClient;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by yum01 on 22/04/2015.
 */
public class Activity1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestServiceClient.bind(this, new TestServiceClient.ServiceConnection() {
            @Override
            public void onServiceConnected(TestServiceClient client) {
                System.out.println(client.test(1, 1f, 1d, Byte.valueOf("1"), false, 's', 1L, "", Collections.<Integer>emptyList()));
                System.out.println(Arrays.toString(client.testArray(new int[]{1, 2, 3})));
            }

            @Override
            public void onServiceDisconnected() {

            }
        }, Context.BIND_AUTO_CREATE);

    }


}
