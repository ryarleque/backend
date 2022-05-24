package com.sportlimacenter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long parentUserId;
    private long childUserId;
}