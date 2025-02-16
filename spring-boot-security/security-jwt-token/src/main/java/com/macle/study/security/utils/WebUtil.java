package com.macle.study.security.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class WebUtil {
    public static String renderString(HttpServletResponse response, String str) {
        try {
            response.setStatus(200);
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
