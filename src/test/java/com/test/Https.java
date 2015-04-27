package com.test;

import com.lamfire.utils.HttpsClient;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Https {

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://itunes.apple.com/cn/app/id791532221");
        HttpsClient https = new HttpsClient();
        https.open(url);
        String ret = https.readAsString("UTF-8");
        System.out.println(ret);
    }
}