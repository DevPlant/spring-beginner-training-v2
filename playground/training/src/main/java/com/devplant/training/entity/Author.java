package com.devplant.training.entity;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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


}
