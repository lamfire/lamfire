package com.test.multicast;

import com.lamfire.json.JSON;
import com.lamfire.utils.MultiCaster;

public class MulitCastTest2 {
    public static void main(String[] args) throws Exception{
        MultiCaster multiCaster = new MultiCaster(9999) {
            public void onMessage(byte[] message) {
                JSON j = JSON.fromBytes(message);
                System.out.println(j);
            }
        };

        multiCaster.startup();

        JSON req = new JSON();
        req.put("id",1001);
        req.put("name","test");
        multiCaster.send(req.toBytes());
    }
}
