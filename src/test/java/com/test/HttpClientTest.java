package com.test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.lamfire.utils.FileUtils;
import com.lamfire.utils.HttpClient;

public class HttpClientTest {

	

	public static void httpMultipart() throws Exception {
		//HttpClient http = new HttpClient("http://192.168.0.117/webkit/demoJson.jhtml");
		HttpClient http = new HttpClient();
		http.setContentType(HttpClient.ContentType.multipart_form_data);
		http.setMethod("POST");
		http.open("http://192.168.3.121:8080/NDFile/image");

		File file = new File("D:\\data\\1.jpg");
		http.sendMultipartFile("file", file);
		http.sendMultipartFinish();
		

		String content = http.readAsString();
		System.out.println(content);
		System.out.println(http.getResponseCode() + " : " +http.getResponseMessage());
		System.out.println(content.length());
		http.close();
	}
	
	public static void httpMultipart1() throws Exception {
		//HttpClient http = new HttpClient("http://192.168.0.117/webkit/demoJson.jhtml");
		HttpClient http = new HttpClient();
		http.setContentType(HttpClient.ContentType.multipart_form_data);
		http.setMethod("POST");
		http.open("http://192.168.3.121:8080/NDFile/image");
		URL url = new URL("http://192.168.3.121/image/201208/d9/d9d4cd80ea9e11e10000117a93f9e0ff_1080_1920.jpg");
		InputStream input = url.openStream();
		http.sendMultipartFileAsStream("file", "11.jpg",input);
		http.sendMultipartFinish();
		input.close();

		String content = http.readAsString();
		System.out.println(content);
		System.out.println(http.getResponseCode() + " : " +http.getResponseMessage());
		System.out.println(content.length());
		http.close();
	}
	
	public static void postTest()throws Exception{
		HttpClient http = new HttpClient();
		http.setMethod("post");
		http.open("http://www.mob.com");
		http.addPostParameter("name", "lamfire");
		http.addPostParameter("password", "lin12345");
		http.addPostParameter("authCode", "lin12345");
		http.post();
		String ret = http.readAsString();
		System.out.println(ret);
	}
	
	public static void mult() throws Exception {
			String url = "http://t1.imanhua.com/Files/Images/120/46805/imanhua_029_201200526.jpg";
			HttpClient client = new HttpClient();
			client.setRequestHeader("Cookie","Hm_lvt_6dbc350428237b030c7de3a18ddadfc4=1322268937295; visited=35; Hm_lpvt_6dbc350428237b030c7de3a18ddadfc4=1322271816303");
			client.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2");
			client.setRequestHeader("Accept-Charset","GBK,utf-8;q=0.7,*;q=0.3");
			client.setRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			client.setRequestHeader("Accept-Encoding","deflate,sdch");
			client.setRequestHeader("Accept-Language","zh-CN,zh;q=0.8");
			client.setMethod("GET");
			client.open(url);
			byte[] data = client.read();
			FileUtils.writeByteArrayToFile(new File("/hanhua1.jpg"), data);
	}
	
	public static void main(String[] args)throws Exception{
		postTest();
	}
}
