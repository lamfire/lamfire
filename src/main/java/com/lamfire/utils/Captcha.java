package com.lamfire.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class Captcha {
    private static final Random random = new Random(System.currentTimeMillis());

    private String[] fontTypes = {"\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66"};

    // 设置备选文字
    private String baseText = "QWERTYUPASDFGHJKZXCVBNM23456789";
    //随机字数
    private int fontCount = 4;

    //干扰线数量
    private int interfereCount = 16;

    // 最大字体
    private int maxFontSize = 32;

    // 最小字体
    private int minFontSize = 16;

    // 保存生成的汉字字符串
    private String captchaCode = "";

    // 设置图片的长宽
    private int width = 120, height = 30;

    private String imageFormatName = "PNG";

    public Captcha() {

    }

    public Captcha(String base) {
        this.baseText = base;
    }

    // 生成随机颜色
    Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }


    public String make(OutputStream output) {

//		 备选汉字的长度
        int length = baseText.length();

        // 创建内存图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics2D g = image.createGraphics();


        // 设定图像背景色(因为是做背景，所以偏淡)
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 备选字体
        int fontTypesLength = fontTypes.length;

        // 随机产生干扰线，使图象中的认证码不易被其它程序探测到

        for (int i = 0; i < interfereCount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(width / 2);
            int yl = random.nextInt(height / 2);
            g.setColor(getRandColor(64, 180));
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 取随机产生的认证码
        int padding = width - width * 4 / 5;
        int perFontX = (width - padding)/ fontCount;
        for (int i = 0; i < this.fontCount; i++) {
            int start = random.nextInt(length);
            String rand = baseText.substring(start, start + 1);
            captchaCode += rand;

            // 设置字体的颜色
            g.setColor(getRandColor(10, 150));
            // 设置字体
            int fontSize = minFontSize + random.nextInt(maxFontSize - minFontSize);
            Font font = new Font(fontTypes[random.nextInt(fontTypesLength)], random.nextInt(2),fontSize);
            //旋转
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(-50 + RandomUtils.nextInt(90)));
            g.setFont(font.deriveFont(affineTransform));

            // 将此字符画到图片上
            int x = padding/2 + i * perFontX + RandomUtils.nextInt((perFontX/2));
            int y = (fontSize + height)/2;
            g.drawString(rand, x, y);
        }

        g.dispose();

        // 输出图象到页面
        try {
            ImageIO.write(image, imageFormatName, output);
        } catch (IOException e) {
        }
        return captchaCode;
    }

    public byte[] makeAsBytes(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        make(outputStream);
        byte[] bytes = outputStream.toByteArray();
        return bytes;
    }

    public void setFontCount(int count) {
        this.fontCount = count;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setMaxFontSize(int maxFontSize) {
        this.maxFontSize = maxFontSize;
    }

    public void setMinFontSize(int minFontSize) {
        this.minFontSize = minFontSize;
    }

    public void setInterfereCount(int interfereCount) {
        this.interfereCount = interfereCount;
    }

    public void setImageFormatName(String imageFormatName) {
        this.imageFormatName = imageFormatName;
    }

    public String[] getFontTypes() {
        return fontTypes;
    }

    public void setFontTypes(String[] fontTypes) {
        this.fontTypes = fontTypes;
    }

    public String getBaseText() {
        return baseText;
    }

    public void setBaseText(String baseText) {
        this.baseText = baseText;
    }

}
