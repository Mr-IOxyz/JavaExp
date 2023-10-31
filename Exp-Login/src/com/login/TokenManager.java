package com.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManager {
    private static Map<String, TokenInfo> tokens = new HashMap<>();
    private static final long TOKEN_VALIDITY_SECONDS = 10; //秒

    public static String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + (TOKEN_VALIDITY_SECONDS * 1000);
        tokens.put(token, new TokenInfo(username, expirationTime));
        return token;
    }

    public static boolean isTokenValid(String token) {
        if (tokens.containsKey(token)) {
            TokenInfo tokenInfo = tokens.get(token);
            if (System.currentTimeMillis() <= tokenInfo.getExpirationTime()) {
                return true;
            }
            tokens.remove(token);
        }
        return false;
    }

    // 内部类用于存储令牌信息
    private static class TokenInfo {
        private String username;
        private long expirationTime;

        TokenInfo(String username, long expirationTime) {
            this.username = username;
            this.expirationTime = expirationTime;
        }

        public String getUsername() {
            return username;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}
