package com.devplant.snippet.jdbc.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trainer {

    private int id;
    private String name;

}
