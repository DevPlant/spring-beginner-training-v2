package com.devplant.training.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


// JPA Annotations
@Table(name = "books")
@Entity
// Lombok Annotations
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "author")
public class Book {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "book_title")
    private String title;

    private int year;

    @ManyToOne(fetch = FetchType.EAGER)
    private Author author;

}
