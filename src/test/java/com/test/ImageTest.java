package com.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.lamfire.utils.Image;
import com.lamfire.utils.ImageScale;

public class ImageTest {

	public static void main(String[] args)throws Exception {
		int w = 300;
		int h = 450;
		
		File file = new File("D:\\data\\12345.jpg");
		BufferedImage bi = Image.decodeJPEG(file);
		Image image = new Image(bi);
		image.zoomScale(w, h);
		image.saveAsJPEG(new File("D:\\data\\12345_1.jpg"),0.8f);
		
		ImageScale scale = new ImageScale();
		BufferedImage srcBufferImage = Image.decodeJPEG(file);
		BufferedImage tarImage = scale.zoomOut(srcBufferImage, w, h);
		ImageIO.write(tarImage, "JPEG", new File("D:\\data\\12345_2.jpg"));
	}
}
