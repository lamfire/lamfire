package com.test;

import com.lamfire.utils.SimpleMailSender;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-28
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class MailSenderTest {

    public static void main(String[] argv) throws Exception {
        SimpleMailSender sender = new SimpleMailSender();
        sender.setDebug(false);//设置调试模式
        sender.setSmtpHost("smtp.163.com");//SMTP地址
        sender.setSmtpAccount("hayash@163.com");//SMTP账户
        sender.setSmtpPassword("******");//SMTP密码
        sender.setMailFrom("hayash@163.com");//发送者地址
        sender.addMailTo("52089489@qq.com");//目标地址
        sender.setSubject("这里是邮件标题");
        sender.setMailContent("这里是邮件内容");
        sender.send();
    }
}
