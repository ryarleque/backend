package com.sportlimacenter.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    private String user;
    private String password;
    private String name;
    private String lastName;
    private String profile;
    private String phone;
    private String email;
    private long branchId;
    private PromoRequest promo;
    private String referralCode;
}
