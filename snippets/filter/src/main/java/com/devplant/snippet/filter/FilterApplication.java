package com.devplant.snippet.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class FilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilterApplication.class, args);
    }

    @RestController
    public static class Controller {

        @GetMapping("/hello")
        public String hello(@RequestParam("name") String name) {
            log.info("Should say hello to: " + name);
            return "Hello, " + name;
        }
    }

    @Slf4j
    @Component
    public static class CustomFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            log.info(" ----> Filter initialized");
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

            log.info("filtering");
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {

                @Override
                public String[] getParameterValues(String name) {
                    if (name.equalsIgnoreCase("name")) {
                        log.info("Original Value Reads: " + Arrays.asList(super.getParameterValues(name)) + " but now it's just Timo!");
                        return new String[]{"Timo"};
                    } else {
                        return super.getParameterValues(name);
                    }

                }


            };
            filterChain.doFilter(wrapper, servletResponse);
        }

        @Override
        public void destroy() {

            log.info(" ---> Filter Destroyed ");
        }
    }

}
