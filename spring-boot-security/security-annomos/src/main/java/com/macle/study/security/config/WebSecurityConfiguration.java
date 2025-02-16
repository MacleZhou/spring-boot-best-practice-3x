package com.macle.study.security.config;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Spring Security 配置项
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalAuthentication
public class WebSecurityConfiguration {

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Resource
    private RestAccessDeniedHandler restAccessDeniedHandler;

    private UserDetailsService userDetailsService;

    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 搜寻 匿名标记 url： PreAuthorize("hasAnyRole('anonymous')") 和 PreAuthorize("@tsp.check('anonymous')") 和 AnonymousAccess
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            PathPatternsRequestCondition pathPatternsCondition = infoEntry.getKey().getPathPatternsCondition();
            Set<String> patternList = new HashSet<>();
            if (null != pathPatternsCondition){
                Set<PathPattern> patterns = pathPatternsCondition.getPatterns();
                for (PathPattern pattern : patterns) {
                    patternList.add(pattern.getPatternString());
                }
            }
            if (null != preAuthorize && preAuthorize.value().toLowerCase().contains("anonymous")) {
                anonymousUrls.addAll(patternList);
            } else if (null != anonymousAccess && null == preAuthorize) {
                anonymousUrls.addAll(patternList);
            }
        }

        httpSecurity
                // 禁用basic明文验证
                .httpBasic(it -> it.disable())
                // 前后端分离架构不需要csrf保护
                .csrf(it -> it.disable())
                // 禁用默认登录页
                .formLogin(it -> it.disable())
                // 禁用默认登出页
                .logout(it -> it.disable())
                // 设置异常的EntryPoint，如果不设置，默认使用Http403ForbiddenEntryPoint
                .exceptionHandling(exceptions -> {
                    // 401
                    exceptions.authenticationEntryPoint(restAuthenticationEntryPoint);
                    // 403
                    exceptions.accessDeniedHandler(restAccessDeniedHandler);
                })
                // 前后端分离是无状态的，不需要session了，直接禁用。
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // 允许匿名访问
                        .requestMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                        // 允许所有OPTIONS请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 允许 SpringMVC 的默认错误地址匿名访问
                        .requestMatchers("/error").permitAll()
                        // 允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                // 加我们自定义的过滤器，替代UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名进行跨域调用
        config.addAllowedOrigin("*");
        // 放行全部原始头信息
        config.addAllowedHeader("*");
        // 允许所有请求方法跨域调用
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);

    }

    @Bean
    public UserDetailsService userDetailsService() {
        //return username -> userDetailsService.loadUserByUsername(username);
        this.userDetailsService = new MyUserDetailsService();
        return this.userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        // 设置密码编辑器
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}