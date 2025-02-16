package com.macle.study.security.config;


import cn.hutool.jwt.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT登录授权过滤器
 */

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        response.setCharacterEncoding("utf-8");
        if (null == authorization){
            // 没有token
            chain.doFilter(request, response);
            return;
        }
        try{
            if (!authorization.startsWith("Bearer ")){
                // token格式不正确
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token格式不正确");
                return;
            }
            boolean verify = MyJWTUtil.verify(authorization);
            if(!verify){
                // token格式不正确
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token验证失败");
                return;
            }
        }catch (Exception e){
            // token格式不正确
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "token验证失败");
            return;
        }
        JWT jwt = MyJWTUtil.parseToken(authorization);
        Object uid = jwt.getPayload("uid");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(uid,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

}