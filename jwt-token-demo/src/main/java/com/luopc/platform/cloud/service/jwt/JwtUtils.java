package com.luopc.platform.cloud.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private final static String secretString = "12345678901234567890123456789012";
    private final static SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    public String generateToken(String userId, String userNick, Map<String, Object> other) {
        // 设置有效时间
        long period = 7200000;
        JwtBuilder jwtBuilder = Jwts.builder()
                .claims(other) // 使用setClaims可以将Map数据进行加密，必须放在其他变量之前
                .id(userId)
                .subject(userNick)
                .expiration(new Date(System.currentTimeMillis() + period)) // 设置有效期
                .signWith(key);
        return jwtBuilder.compact();
    }

    public static Claims parseToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println(key.getEncoded().length);
    }
}
