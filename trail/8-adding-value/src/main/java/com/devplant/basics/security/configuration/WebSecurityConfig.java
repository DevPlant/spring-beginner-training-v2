package com.devplant.basics.security.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**", "/h2-console/**").permitAll()
                .and().httpBasic()
                .and().formLogin().disable()
                .csrf().disable();

        http.headers().frameOptions().disable();

        String[] NO_AUTH_API_PATHS = {"/api/user/register", "/api/book/**", "/api/author/**"};
        String[] USER_REQUIRED_API_PATHS = {"/api/reservation/**", "/api/user/**"};
        String[] ADMIN_REQUIRED_API_PATHS = {"/api/admin/**"};

        http.authorizeRequests()
                .antMatchers(NO_AUTH_API_PATHS).permitAll();

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, USER_REQUIRED_API_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, USER_REQUIRED_API_PATHS).hasAnyRole("USER", "ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.POST, USER_REQUIRED_API_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.POST, USER_REQUIRED_API_PATHS).hasAnyRole("USER", "ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.DELETE, USER_REQUIRED_API_PATHS)
                .authenticated().and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, USER_REQUIRED_API_PATHS).hasAnyRole("USER", "ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.GET, ADMIN_REQUIRED_API_PATHS)
                .authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, ADMIN_REQUIRED_API_PATHS).hasAnyRole("ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.DELETE, ADMIN_REQUIRED_API_PATHS)
                .authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, ADMIN_REQUIRED_API_PATHS).hasRole("ADMIN");

        http
                .authorizeRequests().antMatchers(HttpMethod.POST, ADMIN_REQUIRED_API_PATHS)
                .authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, ADMIN_REQUIRED_API_PATHS).hasRole("ADMIN");
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication()
                .authoritiesByUsernameQuery("select u.username, r.role from users u inner join roles r on u.username = r.username where u.username = ?")
                .usersByUsernameQuery("select username, password, enabled from users where username = ?")
                .dataSource(dataSource).passwordEncoder(passwordEncoder());
    }


}