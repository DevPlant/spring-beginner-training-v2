package com.devplant.snippets.usefull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateJsonService implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        DataClass myObject = DataClass.builder().name("object").number(42).dataList(Arrays.asList("some", "data")).build();
        myObject.setSubDataList(Arrays.asList(SubDataClass.builder().name("sub 1").build(), SubDataClass.builder().name("sub 2").build()));

        String jsonString = objectMapper.writeValueAsString(myObject);

        log.info("Object as JSON: \n" + jsonString);

        // it works both ways

        DataClass fromString = objectMapper.readValue(jsonString,DataClass.class);

        log.info("From JSON String: "+fromString);
    }


    @Data
    @Builder
    public static class DataClass {
        private String name;
        private int number;
        private List<String> dataList = new ArrayList<>();
        private List<SubDataClass> subDataList = new ArrayList<>();
    }

    @Data
    @Builder
    public static class SubDataClass {
        private String name;
    }
}
