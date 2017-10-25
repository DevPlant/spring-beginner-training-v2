package com.devplant.snippets.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("custom.props")
public class CustomProperties {

    private String aMessage;

    private List<String> aList = new ArrayList<>();

    private List<Nested> nested = new ArrayList<>();

    @Data
    public static class Nested{

        private String name;
        private int id;
    }
}
