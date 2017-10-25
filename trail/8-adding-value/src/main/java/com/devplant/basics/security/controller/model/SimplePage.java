package com.devplant.basics.security.controller.model;


import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplePage<T>  {

    private long totalHits;
    private int page;
    private int pageSize;

    private List<T> data;

}
