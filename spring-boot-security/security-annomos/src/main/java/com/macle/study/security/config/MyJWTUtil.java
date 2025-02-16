package com.macle.study.security.config;


import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class MyJWTUtil extends JWTUtil {


    /**
     * 解析JWT Token
     *
     * @param token token
     * @return {@link JWT}
     */
    public static boolean verify(String token) {
        return verify(token, "LOGIN_TOKEN_KEY_20240410".getBytes());
    }

    /**
     * 解析JWT Token
     *
     * @param token token
     * @return {@link JWT}
     */
    public static boolean verify(String token, byte[] key) {
        if(StrUtil.isNotEmpty(token)){
            if(token.startsWith("Bearer ")){
                token = token.split("Bearer ")[1].trim();
            }
        }
        return JWT.of(token).setKey(key).verify();
    }

    /**
     * 解析JWT Token
     *
     * @param token token
     * @return {@link JWT}
     */
    public static JWT parseToken(String token) {
        if(StrUtil.isNotEmpty(token)){
            if(token.startsWith("Bearer ")){
                token = token.split("Bearer ")[1].trim();
            }
        }
        return JWT.of(token);
    }

    public static String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader("Authorization");
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            return requestHeader.substring(7);
        }
        return null;
    }

    public static String createToken(String userId) {
        Map<String, Object> payload = new HashMap<>(4);
        payload.put("uid", userId);
        payload.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 8);
        return JWTUtil.createToken(payload, "LOGIN_TOKEN_KEY_20240410".getBytes());
    }
}