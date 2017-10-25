package com.devplant.basics.persistence.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@ToString(exclude = "authoredBooks")
public class Author {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private Set<Book> authoredBooks;
}
