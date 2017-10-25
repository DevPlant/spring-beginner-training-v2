package com.devplant.snippets.testing;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecuredService {

    @PreAuthorize("hasRole('ADMIN')")
    public String onlyAdmin(){
        return "Only admin can call this";
    }

}
