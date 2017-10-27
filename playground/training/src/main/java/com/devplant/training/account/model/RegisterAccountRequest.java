package com.devplant.training.account.model;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class RegisterAccountRequest {

    private String username;

    @Min(8)
    private String password;
}
