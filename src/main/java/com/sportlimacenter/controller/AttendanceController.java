package com.sportlimacenter.controller;

import com.sportlimacenter.model.request.CreateAttendanceRequest;
import com.sportlimacenter.model.response.CreateAttendanceResponse;
import com.sportlimacenter.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public CreateAttendanceResponse createBulkAttendance(@RequestBody CreateAttendanceRequest request) {
        return attendanceService.createBulkAttendance(request.getAttendances());
    }
}
