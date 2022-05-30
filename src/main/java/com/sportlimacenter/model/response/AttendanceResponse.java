package com.sportlimacenter.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AttendanceResponse {

    private String branch;
    private LocalDate date;
    private boolean attended;
}
