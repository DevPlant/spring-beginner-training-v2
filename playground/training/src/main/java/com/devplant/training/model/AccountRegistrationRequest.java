package com.devplant.training.model;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountRegistrationRequest {

    private String username;

    @Size(min = 6)
    private String password;
}
