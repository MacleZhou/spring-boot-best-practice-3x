package com.sgz.cg.security.handle;

import cn.hutool.json.JSONUtil;
import com.sgz.cg.entry.DTO.ResultDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        ResultDTO resultDTO = ResultDTO.error("用户名或密码错误");

        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(resultDTO).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}