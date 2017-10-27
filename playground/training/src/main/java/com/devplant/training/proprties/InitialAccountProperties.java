package com.devplant.training.proprties;


import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.devplant.training.entity.Account;

import lombok.Data;

//lombok
@Data
// Spring
@ConfigurationProperties("devplant.init")
public class InitialAccountProperties {

    private List<Account> accounts = new ArrayList<>();

}
