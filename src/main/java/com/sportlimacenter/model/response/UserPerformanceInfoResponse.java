package com.sportlimacenter.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserPerformanceInfoResponse {

    private long id;
    private LocalDate date;
    private long userId;
    private double km;
    private double calories;
}
