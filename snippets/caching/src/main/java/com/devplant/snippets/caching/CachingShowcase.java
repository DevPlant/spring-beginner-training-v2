package com.devplant.snippets.caching;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CachingShowcase implements CommandLineRunner {

    @Autowired
    private RandomService randomService;

    @Override
    public void run(String... strings) throws Exception {


        for(int i =0;i<3;i++){
            log.info(randomService.getRandomMessage());
        }


        for(int i =0;i<3;i++){
            randomService.evictMessageCache();
            log.info(randomService.getRandomMessage());
        }

    }


    @Service
    public static class RandomService {


        @Cacheable("message")
        public String getRandomMessage() {
            return "This is message: " + new Random().nextInt();
        }

        @CacheEvict("message")
        public void evictMessageCache() {

        }
    }
}
