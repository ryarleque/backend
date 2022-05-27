package com.sportlimacenter.model.request;

import lombok.Data;

@Data
public class UserPerformanceInfoRequest {

    private long userId;
    private double km;
    private double calories;
}
