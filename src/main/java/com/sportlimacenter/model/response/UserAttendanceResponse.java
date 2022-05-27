package com.sportlimacenter.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserAttendanceResponse {

    private long id;
    private long userId;
    private LocalDate date;
}
