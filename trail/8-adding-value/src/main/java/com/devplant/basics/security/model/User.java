package com.devplant.basics.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "roles",
            joinColumns = @JoinColumn(name = "username")
    )
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<BookStock> reservedBooks = new ArrayList<>();

    @PostRemove
    protected void postRemove() {
        for (BookStock stock : reservedBooks) {
            // The books which the user didn't take can be reset
            if (!stock.isPickedUp()) {
                stock.setUser(null);
                stock.setApproved(false);
                stock.setOverdue(false);
                stock.setAvailable(true);
                stock.setRequestDate(null);
                stock.setLatestPickupDate(null);
                stock.setLatestReturnDate(null);
                stock.setLastReturnOverDueNotificationDate(null);
            }
        }
    }
}
