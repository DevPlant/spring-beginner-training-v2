package com.devplant.snippets.jpa.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "trainer")
public class Student {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Trainer trainer;

    @PreRemove
    protected void preRemove() {
        log.info("This is called before removing an entity: " + toString());
    }

    @PostRemove
    protected void postRemove() {
        log.info("This is called after removing an entity: " + toString());
    }


    @PreUpdate
    protected void preUpdate() {
        log.info("This is called before updating an entity: " + toString());
    }

    @PostUpdate
    protected void postUpdate() {
        log.info("This is called after updating an entity: " + toString());
    }

    @PrePersist
    protected void prePersist() {
        log.info("This is called before persisting an entity: " + toString());
    }


    @PostPersist
    protected void postPersist() {
        log.info("This is called after persisting an entity: " + toString());
    }


}
