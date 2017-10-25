package com.devplant.basics.apiexplorer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**").permitAll()
                .and().httpBasic()
                .and().formLogin().disable()
                .csrf().disable();

        String[] API_PATHS = {"/api/**"};

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, API_PATHS)
                .authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, API_PATHS).hasAnyRole("USER", "ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.DELETE, API_PATHS)
                .authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, API_PATHS).hasRole("ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.POST, API_PATHS)
                .authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, API_PATHS).hasRole("ADMIN");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER").and()
                .withUser("admin").password("admin").roles("ADMIN");
    }
}