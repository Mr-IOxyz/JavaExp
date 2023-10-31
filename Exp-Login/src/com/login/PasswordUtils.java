package com.login;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    public static String hashPasswordWithUsernameAsSalt(String password, String username) {
        String salt = username; // 在这里，我们直接使用用户名作为盐值

        try {
            // 创建MessageDigest实例并指定SHA-256作为算法
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 将盐值添加到消息摘要
            md.update(salt.getBytes(StandardCharsets.UTF_8));

            // 完成哈希计算
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // 将字节数组转换为字符串，并返回
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // 如果指定的算法不存在，则处理异常
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }
}
