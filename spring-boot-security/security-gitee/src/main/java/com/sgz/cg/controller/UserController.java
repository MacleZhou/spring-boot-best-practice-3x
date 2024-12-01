package com.sgz.cg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sgz.cg.entry.DTO.ResultDTO;
import com.sgz.cg.entry.DTO.UserLoginDTO;
import com.sgz.cg.entry.User;
import com.sgz.cg.service.UserService;
import com.sgz.cg.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping(path = "/user", produces = "application/json;charset=utf-8")
@RequestMapping(path = "/user")
public class UserController {
    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResultDTO login(@RequestBody @Validated UserLoginDTO userLoginDTO, HttpServletResponse response) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userLoginDTO.getUsername()));
        if (user == null) {
            return ResultDTO.error("用户名不存在");
        }

        if (!user.getPassword().equals(password)) {
            return ResultDTO.error("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(username);
        response.setHeader(JwtUtil.HEADER, token);
        response.setHeader("Access-control-Expost-Headers", JwtUtil.HEADER);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResultDTO.success(map);
    }

    //@PreAuthorize配合@EnableGlobalMethodSecurity(prePostEnabled = true)使用
    @PreAuthorize("hasAuthority('/user/logout')")
    @GetMapping("/logout")
    public ResultDTO logout(HttpServletRequest request, HttpServletResponse response) {
        // 退出登录
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            //清除认证
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResultDTO.success();
    }
}