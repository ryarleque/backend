package com.sportlimacenter.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CreatePerformanceInfoRequest {

    List<UserPerformanceInfoRequest> userInfoPerformance;

}
