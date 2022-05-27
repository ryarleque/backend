package com.sportlimacenter.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateAttendanceRequest {

    private List<Long> userIds;

}
