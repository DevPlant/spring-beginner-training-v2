package com.devplant.snippet.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {


    @GetMapping("/echo/1")
    public String echo1(@RequestParam("message") String message) {
        return "Echo: " + message;
    }

    @GetMapping("/echo/2")
    public String echo2(@RequestParam String message) {
        return "Echo: " + message;
    }

    @GetMapping("/variable/1/{variableName}")
    public String pathVar1(@PathVariable("variableName") String anything) {
        return "Variable : " + anything;
    }

    @GetMapping("/variable/2/{anything}/{other}")
    public String pathVar2(@PathVariable String anything, @PathVariable String other) {
        return "Variable : " + anything + " other " + other;
    }

    @GetMapping("/json")
    public MessageObject json(@RequestParam("message") String message) {
        return new MessageObject(message);
    }

    @PostMapping("/post/json")
    public String json(@RequestBody MessageObject messageObject) {
        return "Got your message: " + messageObject.message;
    }

    @DeleteMapping("/same-path/{id}")
    public String delete(@PathVariable("id") String id) {
        log.info("Deleted: " + id);
        return "deleted: " + id;
    }

    @GetMapping("/same-path/{id}")
    public String get(@PathVariable("id") String id) {
        log.info("Got: " + id);
        return id;
    }

    @GetMapping("/same-path")
    public String get() {
        log.info("No Id specified");
        return "No Id specified";
    }

    @GetMapping("/error")
    public void error() {
        throw new CustomRestException("this is an error made pretty !");
    }

    @GetMapping("/old")
    public void getOldFashioned(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HashMap<String, List<String>> convertedForPrint = new HashMap<>();
        request.getParameterMap().forEach((k, v) ->
                convertedForPrint.put(k, Arrays.asList(v))
        );

        response.getWriter().append("It Works, your parameters are: " + convertedForPrint);
    }


    @PostMapping("/same-path")
    public void post(@RequestBody MessageObject messageObject) {
        log.info("Posted: " + messageObject);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = CustomRestException.class)
    @ResponseBody
    public String handleCustomRestException(CustomRestException e) {
        return e.getMessage().toUpperCase() + " There, it's pretty now!";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageObject {

        private String message;
    }

}
