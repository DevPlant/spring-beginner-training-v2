package com.devplant.training;


import org.springframework.stereotype.Service;

@Service
public class GenericService {

    private final boolean stateIsBad = true;

    public String getMessage() {
        if(stateIsBad) {
            return "Hello from Service!";
        }else{
            return "?????";
        }
    }
}
