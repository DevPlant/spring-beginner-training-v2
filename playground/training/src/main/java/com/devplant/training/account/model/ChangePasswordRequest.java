package com.devplant.training.account.model;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String oldPassword;

    @Size(min = 8)
    private String newPassword;
}
