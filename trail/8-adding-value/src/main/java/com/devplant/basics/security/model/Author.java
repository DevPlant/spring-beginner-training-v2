package com.devplant.basics.security.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "authoredBooks")
public class Author {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    @Column(length = 4000)
    private String bio;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Set<Book> authoredBooks;
}
