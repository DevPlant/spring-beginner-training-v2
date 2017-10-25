package com.devplant.snippets.beans;

import org.springframework.stereotype.Service;

@Service
public class MagicBean {

    private String response = "I can't :( I'm a fraud!";

    public String doMagic() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
