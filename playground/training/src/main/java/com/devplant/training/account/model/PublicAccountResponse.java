package com.devplant.training.account.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicAccountResponse {

    private String username;
    private List<String> roles;
}
