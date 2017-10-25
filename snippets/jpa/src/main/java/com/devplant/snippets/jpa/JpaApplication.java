package com.devplant.snippets.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

    @Autowired
    private JpaService jpaService;

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        log.info(" ----> Initializing");
        jpaService.initialize();

        log.info(" ----> Finding all without transaction");
        jpaService.findAllNoTransaction();

        log.info(" ----> Finding all with transaction");
        jpaService.findAllTransaction();

        log.info(" ----> No Transaction required for eager fetch");
        jpaService.noTransactionNeeded();

        log.info(" ----> Experimenting queries");
        jpaService.queries();

        log.info(" ----> Count all");
        jpaService.printCurrentCounts();

        log.info(" ----> Delete transaction fail");
        try {
            jpaService.deleteAllInTransactionFail();
        } catch (Exception e) {
            log.info(" ----> Transaction should be rolled back ... ");
        }

        log.info(" ----> Count all");
        jpaService.printCurrentCounts();

        log.info(" ----> Delete without transaction fail");
        try {
            jpaService.deleteAllWithoutTransactionFail();
        } catch (Exception e) {
            log.info(" ----> No rollback");
        }

        log.info(" ----> Count all");
        jpaService.printCurrentCounts();

    }
}
