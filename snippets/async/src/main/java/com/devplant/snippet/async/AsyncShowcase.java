package com.devplant.snippet.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsyncShowcase implements CommandLineRunner {

    @Autowired
    private AsyncService asyncService;

    @Override
    public void run(String... strings) throws Exception {


        List<CompletableFuture<String>> all = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            all.add(asyncService.withReturn(i));
        }

        CompletableFuture.allOf(all.toArray(new CompletableFuture[0])).get();

        for (CompletableFuture<String> future : all) {
            log.info("Result: " + future.get());
        }

        for (int i = 0; i < 20; i++) {
            asyncService.noReturn(i);
        }

    }


    @Slf4j
    @Service
    public static class AsyncService {

        @Async
        public void noReturn(int i) throws InterruptedException {
            Thread.sleep(500);
            log.info("This is message: " + i);
        }

        @Async
        public CompletableFuture<String> withReturn(int i) throws InterruptedException {
            Thread.sleep(500);

            log.info("With return from: " + i);
            return CompletableFuture.completedFuture("this is: " + i);
        }

    }
}
