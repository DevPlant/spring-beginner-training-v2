package com.devplant.snippet.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin().and().csrf().disable();

        String[] USER_PATHS = {"/user"};
        String[] ADMIN_PATHS = {"/admin"};
        String[] BOTH_PATHS = {"/both", "/who"};
        String[] PUBLIC_PATHS = {"/public"};

        http.authorizeRequests()
                .antMatchers(PUBLIC_PATHS).permitAll();

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, USER_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, USER_PATHS).hasAnyRole("USER");

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, ADMIN_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, ADMIN_PATHS).hasAnyRole("ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, BOTH_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, BOTH_PATHS).hasAnyRole("USER", "ADMIN");

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("timo").password("timo").roles("USER").and()
                .withUser("admin").password("admin").roles("ADMIN");
    }

}
