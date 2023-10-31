package com.login;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {
    public static String generateVerificationCode() {
        // 生成6位随机数作为验证码
        Random random = new Random();
        int verificationCode = 100000 + random.nextInt(900000); // This will generate six digit random Number.
        return String.valueOf(verificationCode);
    }

    public static void sendEmail(String recipient, String subject, String content) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.qq.com"); // 请替换为您的SMTP服务器
        properties.put("mail.smtp.port", "587");

        // 你的邮箱和密码
        final String myAccountEmail = "89302121@qq.com";
        final String password = "rdlmuzqlaocabgjh";

        // 创建一个会话对象和邮件服务器的交互会话
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        // 创建电子邮件消息
        Message message = prepareMessage(session, myAccountEmail, recipient, subject, content);

        // 发送消息
        Transport.send(message);
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recipient, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(content);
            return message;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
