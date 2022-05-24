package com.sportlimacenter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String password;
    private String dni;
    private String lastname;
    private String profile;
    private String phone;
    private boolean enabled;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String email;
    private long branchId;
    private boolean isReferred;

}