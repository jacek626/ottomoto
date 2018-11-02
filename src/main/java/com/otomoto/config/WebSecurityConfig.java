package com.otomoto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                	.antMatchers("/**/favicon.ico", "/css/**","/js/**","/images/**").permitAll()
                    .antMatchers("/", "/home","/admin/register","/registerAdmin","/admin/registerAdmin","/register","admin/register").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginPage("/register")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }
}
