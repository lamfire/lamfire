package com.test;

import com.lamfire.code.Base62;
import com.lamfire.code.PUID;
import com.lamfire.utils.Asserts;

/**
 * Created by lamfire on 16/3/25.
 */
public class PUIDTest {
    public static void main(String[] args) {
        for (int i = 0; i < 9999999; i++) {
            PUID p = PUID.make();
            System.out.println(p.toHexString() + "         " +p.toString()  +"           " + Base62.toBase62String(p.toBytes())) ;
        }

        long v = -9999111001l;
        String b62 = Base62.encode(v);
        long r = Base62.decode(b62);
        System.out.println(b62);
        System.out.println(r);
        Asserts.equalsAssert(v,r);

    }
}
