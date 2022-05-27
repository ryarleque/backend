package com.sportlimacenter.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserResponse {

    private long id;
    private String fullName;
    private String dni;
    private String email;
    private LocalDate startDate;
    private String phone;
    private int remainingDays;

}
