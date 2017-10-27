package com.devplant.snippets.jpa;


import java.util.List;

import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devplant.snippets.jpa.model.Student;
import com.devplant.snippets.jpa.model.Trainer;
import com.devplant.snippets.jpa.repo.StudentRepository;
import com.devplant.snippets.jpa.repo.TrainerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JpaService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TrainerRepository trainerRepository;


    @Transactional
    public void initialize() {
        studentRepository.deleteAll();
        trainerRepository.deleteAll();

        Trainer timo = trainerRepository.save(Trainer.builder().name("timo").build());
        Trainer radu = trainerRepository.save(Trainer.builder().name("radu").build());

        studentRepository.save(Student.builder().name("Generic Student  1").trainer(timo).build());
        studentRepository.save(Student.builder().name("Generic Student  2").trainer(timo).build());
        studentRepository.save(Student.builder().name("Generic Student  3").trainer(radu).build());
    }

    public void findAllNoTransaction() {

        try {
            trainerRepository.findAll().forEach(trainer ->
                    log.info(trainer.getName() + " trains " + trainer.getStudents()));
        } catch (LazyInitializationException e) {
            log.error("You can't do this without a transaction");
        }
    }

    @Transactional
    public void findAllTransaction() {
        trainerRepository.findAll().forEach(trainer
                -> log.info(trainer.getName() + " trains " + trainer.getStudents()));
    }


    @Transactional
    public void queries() {
        Trainer timo = trainerRepository.findByName("timo");
        log.info("Found by name: " + timo.getName());

        List<Student> timosStudents = studentRepository.findByTrainerName("timo");

        timosStudents.forEach(s -> log.info(s + " trained by " + s.getTrainer()));

        List<Student> radusStudents = studentRepository.findByTrainerName("radu");

        radusStudents.forEach(s -> log.info(s + " trained by " + s.getTrainer()));

        Trainer timoJpa = trainerRepository.findByNameUsingJpaQuery("timo");

        log.info("Found by name using jpa query: " + timoJpa.getName() + " he's training: "+timoJpa.getStudents());

        Trainer timoNative = trainerRepository.findByNameUsingNativeQuery("timo");

        log.info("Found by name using native query: " + timoNative.getName()+ " he's training: "+timoJpa.getStudents());
    }

    public void noTransactionNeeded() {
        studentRepository.findAll().forEach(student -> {
            log.info(student.getName() + " is trained by: " + student.getTrainer().getName());
        });
    }

    @Transactional
    public void deleteAllInTransactionFail() {
        studentRepository.deleteAll();
        if (true) {
            throw new RuntimeException("Fail");
        }
        trainerRepository.deleteAll();
    }

    public void deleteAllWithoutTransactionFail() {
        studentRepository.deleteAll();
        if (true) {
            throw new RuntimeException("Fail");
        }
        trainerRepository.deleteAll();
    }

    public void printCurrentCounts() {
        log.info(trainerRepository.count() + " Trainers");
        log.info(studentRepository.count() + " Students");
    }

}
