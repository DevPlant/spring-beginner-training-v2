package com.devplant.snippets.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class BeansShowCaseNumber1 implements CommandLineRunner {

    @Autowired
    private MagicBean magicBean;

    @Override
    public void run(String... strings) throws Exception {

        log.info(" ------> " + magicBean.doMagic());
        magicBean.setResponse("We'll do some magic");

    }

}
