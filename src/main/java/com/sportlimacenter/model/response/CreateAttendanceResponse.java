package com.sportlimacenter.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateAttendanceResponse {

    private List<UserAttendanceResponse> attendanceList;
    private List<UserPerformanceInfoResponse> performanceList;
}
