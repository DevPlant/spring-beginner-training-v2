package com.devplant.training.configuration;

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

import com.devplant.training.repo.AccountRepo;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // for h2 console IFRAME
        httpSecurity.headers().frameOptions().disable();

        // Enable/Disable Http Basic
        // httpSecurity.httpBasic();
        // Enable/Disable Form Login
        httpSecurity.formLogin();
        // Enable/Disable CSRF ( just so you can test your Calls! )
        httpSecurity.csrf().disable();

        configureAppSpecificHttpSecurityPaths(httpSecurity);

    }

    public static void configureAppSpecificHttpSecurityPaths(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // allow everything on swagger, the other ones are just resources swagger loads, nothing 'dangerous here'
                .antMatchers(
                        "/h2-console/**",
                        "/swagger-ui.html", "/webjars/**",
                        "/swagger-resources/**", "/v2/**").permitAll();
        // Custom Config follows ! we'll write this
        String[] ACCOUNT_PATHS = {"/account/**"};
        String[] ACCOUNT_REGISTER_PATH = {"/account/register"};
        String[] API_PATHS = {"/book/**", "/author/**", "/*"};

        httpSecurity.authorizeRequests().antMatchers(ACCOUNT_REGISTER_PATH).permitAll();

        httpSecurity.authorizeRequests().antMatchers(ACCOUNT_PATHS).authenticated();

        httpSecurity.authorizeRequests().antMatchers(
                HttpMethod.GET, API_PATHS).authenticated()
                .anyRequest().hasAnyRole("ADMIN", "USER");

        httpSecurity.authorizeRequests().antMatchers(
                HttpMethod.POST, API_PATHS).authenticated()
                .anyRequest().hasAnyRole("ADMIN");

        httpSecurity.authorizeRequests().antMatchers(
                HttpMethod.DELETE, API_PATHS).authenticated()
                .anyRequest().hasAnyRole("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Tell Spring to Use in-memmory user database with 2 pre-set users,
        // since we don't have a User Entity/Table yet!

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .authoritiesByUsernameQuery("select account_username,roles from account_roles where account_username = ?")
                .usersByUsernameQuery("select username,password,enabled from accounts where username = ?");
    }
}