package com.test;

import com.lamfire.utils.Captcha;
import com.lamfire.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CaptchaTest {

    public static void main(String[] args) throws Exception{
        Captcha c = new Captcha();
        c.setWidth(80);
        c.setHeight(40);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String capcode = c.make(outputStream);
        byte[] bytes = outputStream.toByteArray();
        FileUtils.writeByteArrayToFile(new File("/data/captcha.jpeg"),bytes);
    }
}
