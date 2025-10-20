package com.luopc.platform.cloud.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtBuilderTest {

    //创建私钥，这里的私钥是建议随机生成的 (建议在实际应用中从安全配置中获取)
    private static final String secretString = "ThisIsASecretKeyForJWTGeneration123";

    @Test
    public void generateJwtToken() {
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        String userId = "123456";
        // 2. 创建JWT并设置claims
        JwtBuilder jwtBuilder = Jwts.builder()
                .id(userId)  // 设置唯一ID
                .subject("user-auth")  // 设置主题
                .issuer("your-application")  // 设置签发者
                .issuedAt(new Date())  // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + 3600000))  // 设置过期时间(1小时后)
                .claim("role", "admin")  // 添加自定义claim
                .signWith(key);  // 使用密钥签名

        // 3. 生成JWT token
        String token = jwtBuilder.compact();
        System.out.println("Generated JWT Token: Bearer " + token);
    }

    @Test
    @Ignore
    public void parseToken() {
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJzdWIiOiJ1c2VyLWF1dGgiLCJpc3MiOiJ5b3VyLWFwcGxpY2F0aW9uIiwiaWF0IjoxNzQ4NjgxNzc3LCJleHAiOjE3NDg2ODUzNzcsInJvbGUiOiJhZG1pbiJ9.uCLQ-Y62sRV_Af7qfalZGFN9DClejfAUFBzvUaLW5ps";

        // 1、解析token，这里的key要和私钥是一个
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        // 2、获取参数
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.get("role"));

    }

    @Test
    public void encryptToken() {
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

        //encryptToken
        JwtBuilder jwtBuilder = Jwts.builder().claim("param_1", "12345").claim("param_2", "9527").signWith(key);
        String token = jwtBuilder.compact();

        System.out.println("Generated JWT Token: Bearer " + token);

        //decryptToken
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        //verify
        Assert.assertEquals("12345", claims.get("param_1"));
        Assert.assertEquals("9527", claims.get("param_2"));

    }

    public static void main(String[] args) {
        System.out.println(System.getenv("deepseek_token"));
    }
}
