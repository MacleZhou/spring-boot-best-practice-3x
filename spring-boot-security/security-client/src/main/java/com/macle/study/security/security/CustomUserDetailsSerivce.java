package com.macle.study.security.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsSerivce implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //模拟从数据库中查询用户信息
        if("user".equals(username)) {
            return User.withUsername("user")
                    .password("{noop}password")
                    .authorities((GrantedAuthority) List.of(new GrantedAuthority() {
                        @Override
                        public String getAuthority() {
                            return "ROLE_USER";
                        }
                    }))
                    .build();
        }
        else if("admin".equals(username)) {
            return User.withUsername("admin")
                    .password("{noop}password")
                    .authorities((GrantedAuthority) List.of(new GrantedAuthority() {
                        @Override
                        public String getAuthority() {
                            return "ROLE_ADMIN";
                        }
                    }))
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
