package com.luopc.platform.cloud.service.jwt.handler;

import com.luopc.platform.cloud.service.jwt.JwtUtils;
import io.jsonwebtoken.Claims;

public class AuthenticationHandler {

    public boolean preHandle(String token) throws Exception {
        Claims claims = JwtUtils.parseToken(token);
        String userId = String.valueOf(claims.get("userId"));
        return true;
    }
}
