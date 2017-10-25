package com.devplant.basics.servicesandrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.servicesandrest.model.GreetingModel;
import com.devplant.basics.servicesandrest.service.GoodbyeService;
import com.devplant.basics.servicesandrest.service.HelloService;

@RestController
@RequestMapping("/api")
public class GreetingController {

    @Autowired
    private GoodbyeService goodbyeService;

    @Autowired
    private HelloService helloService;

    @RequestMapping("/hello")
    public GreetingModel sayHello(@RequestParam String who) {
        return new GreetingModel(helloService.sayHello(who));
    }

    @RequestMapping("/goodbye")
    public GreetingModel sayGoodbye() {
        return new GreetingModel(goodbyeService.sayGoodbye());
    }

}