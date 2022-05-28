package com.sportlimacenter.controller;

import com.sportlimacenter.model.request.CreateAttendanceRequest;
import com.sportlimacenter.model.request.CreatePerformanceInfoRequest;
import com.sportlimacenter.model.response.CreateAttendanceResponse;
import com.sportlimacenter.model.response.CreatePerformanceInfoResponse;
import com.sportlimacenter.model.response.UserPerformanceInfoResponse;
import com.sportlimacenter.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public CreatePerformanceInfoResponse createBulkAttendance(@RequestBody CreatePerformanceInfoRequest request) {
        return performanceService.createBulkPerformanceInfo(request.getUserInfoPerformance());
    }

    @GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserPerformanceInfoResponse> getPerformanceInfoByDNI(@RequestParam(name = "dni", required = false) String dni) {
        return performanceService.getPerformanceInfoByDNI(dni);
    }
}
