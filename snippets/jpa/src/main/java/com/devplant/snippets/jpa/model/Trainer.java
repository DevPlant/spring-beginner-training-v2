package com.devplant.snippets.jpa.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Entity
@Builder
@Table(name = "trainer")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "students")
public class Trainer {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trainer")
    private List<Student> students = new ArrayList<>();


}
