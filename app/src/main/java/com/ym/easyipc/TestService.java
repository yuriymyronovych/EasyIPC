package com.ym.easyipc;

import com.ym.easyipc.processor.EasyIPCMethod;
import com.ym.easyipc_api.lib.EasyIPCService;

import java.util.List;

/**
 * Created by yum01 on 21/04/2015.
 */
public class TestService extends EasyIPCService {

    @EasyIPCMethod
    public int test(int a, float b, double c, byte d, boolean e, char f, long g, String h, List<Integer> j) {
        return 1;
    }

    @EasyIPCMethod
    public int[] testArray(int[] a) {
        return a;
    }

    public int noMethod(int c){return 0;};
}
