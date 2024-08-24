package com.example.managebackend.config;


import com.example.managebackend.utils.SecurityUtils;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] permitUrls = {
                "/auth/login",
                "/auth/user-name/exists",
                "/auth/register"
        };
        SecurityUtils.setDefaultHttpSecurity(http, permitUrls);
    }

}
