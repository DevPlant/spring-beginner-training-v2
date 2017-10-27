package com.devplant.training.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// JPA
@Entity
// Lombok
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "books")
public class Author {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    private String bio;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();


    @PreRemove
    public void preRemove() {
        books.forEach(book -> book.setAuthor(null));
    }

    @PostRemove
    public void postRemove() {

    }

    @PrePersist
    public void prePersist() {
        log.info("---> Creating author: " + name);
    }

    @PostPersist
    public void postPersist() {

    }

    @PreUpdate
    public void preUpdate() {

    }

    @PostUpdate
    public void postUpdate() {

    }


}
