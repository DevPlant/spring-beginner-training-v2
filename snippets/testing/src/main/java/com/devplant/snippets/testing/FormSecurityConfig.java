package com.devplant.snippets.testing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("form")
@Configuration
public class FormSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin();

        String[] USER_PATHS = {"/user"};

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, USER_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, USER_PATHS).hasAnyRole("USER");

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("timo").password("timo").roles("USER").and()
                .withUser("admin").password("admin").roles("ADMIN");
    }

}
