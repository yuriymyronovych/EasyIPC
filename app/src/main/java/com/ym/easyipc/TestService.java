package com.ym.easyipc;

import com.ym.easyipc.processor.EasyIPCMethod;
import com.ym.easyipc_api.lib.EasyIPCService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yum01 on 21/04/2015.
 */
public class TestService extends EasyIPCService {
    private List<IListener> listeners = new ArrayList<IListener>();

    @EasyIPCMethod
    public int test(int a, float b, double c, byte d, boolean e, char f, long g, String h, List<Integer> j) {
        return 1;
    }

    @EasyIPCMethod
    public int[] testArray(int[] a) {
        return a;
    }

    @EasyIPCMethod
    public void testEmpty() {

    }



    @EasyIPCMethod
    public void testAddListener(final IListener listener) {
        listeners.add(listener);
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("sending to listener: " + i);
                    listeners.get(0).onResult(i);
                }
            }
        }.start();
    }

    @EasyIPCMethod
    public void testRemoveListener(IListener listener) {
        listeners.remove(listener);
    }

    public int noMethod(int c){return 0;};
}
