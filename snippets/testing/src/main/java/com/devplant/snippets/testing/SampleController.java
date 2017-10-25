package com.devplant.snippets.testing;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/user")
    public String userOnly() {
        return "Only a User can access this";
    }

}
