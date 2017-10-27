package com.devplant.training.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//JPA annotations
@Entity
@Table(name = "accounts")

// Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "username")
public class Account {

    @Id
    private String username;

    private String password;

    private boolean enabled;
}
