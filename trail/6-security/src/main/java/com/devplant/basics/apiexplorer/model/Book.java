package com.devplant.basics.apiexplorer.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "author")
public class Book {

    @Id
    @GeneratedValue
    private long id;

    private int year;
    private String name;

    @Column(length = 4000)
    private String synopsis;
    private String isbn;

    @ManyToOne(fetch = FetchType.EAGER)
    private Author author;

}
