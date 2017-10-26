package com.devplant.snippets.bankingapp;


import java.security.Principal;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage("/login").and();
        http.csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("timo").password("timo").roles("USER").and();
    }

    @Controller
    @EnableWebMvc
    public static class RouteController {

        @GetMapping("/login")
        public String greeting() {
            return "login";
        }

        @GetMapping
        public String index(Principal principal){
            if(principal == null){
                return "redirect:/login";
            }
            return "index";
        }

        @GetMapping("/")
        public String root(Principal principal){
            if(principal == null){
                return "redirect:/login";
            }
            return "index";
        }

    }

}
