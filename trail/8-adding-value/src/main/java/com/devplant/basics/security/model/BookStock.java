package com.devplant.basics.security.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "user")
public class BookStock {

    @Id
    @GeneratedValue
    private long id;

    private boolean available = true;
    private boolean approved;
    private boolean pickedUp;

    private boolean overdue;

    private LocalDateTime requestDate;
    private LocalDateTime latestPickupDate;
    private LocalDateTime latestReturnDate;

    private LocalDateTime lastReturnOverDueNotificationDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
}
