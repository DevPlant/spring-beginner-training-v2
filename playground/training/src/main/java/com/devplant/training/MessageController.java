package com.devplant.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MessageController {

    @Autowired
    protected GenericService service;

    @GetMapping(value = "/hello")
    public Message hello() {
        Message m = Message.builder()
                .value(service.getMessage())
                .id(1)
                .build();

        log.info("Amazing!");

        return m;
    }

    @PostMapping(value = "/hello", consumes = {"application/json"})
    public Message post(@RequestBody Message message) {
        message.setValue("We change the  value: " + message.getValue());
        return message;
    }

    @DeleteMapping("/hello/{id}")
    public String justDelete(@PathVariable("id") int identifier) {
        log.info("Deleting message: " + identifier);
        return "Deleting message: " + identifier;
    }

    @GetMapping("/query")
    public String query(@RequestParam(value = "q",required = false,
            defaultValue = "42") String query) {
        return "You searched for : " + query;
    }


}
