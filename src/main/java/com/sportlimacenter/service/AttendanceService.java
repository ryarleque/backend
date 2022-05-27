package com.sportlimacenter.service;

import com.sportlimacenter.entity.Attendance;
import com.sportlimacenter.entity.Performance;
import com.sportlimacenter.entity.User;
import com.sportlimacenter.model.request.UserAttendanceRequest;
import com.sportlimacenter.model.response.CreateAttendanceResponse;
import com.sportlimacenter.model.response.UserAttendanceResponse;
import com.sportlimacenter.model.response.UserPerformanceInfoResponse;
import com.sportlimacenter.repository.AttendanceRepository;
import com.sportlimacenter.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional
    public CreateAttendanceResponse createBulkAttendance(List<UserAttendanceRequest> userAttendanceRequestList) {
        LocalDate now = LocalDate.now();
        List<Attendance> attendanceList = userAttendanceRequestList.stream()
                .map(uar -> Attendance.builder()
                        .date(now)
                        .user(User.builder().id(uar.getUserId()).build())
                        .attended(true)
                        .build())
                .collect(Collectors.toList());
        List<Attendance> savedAttendanceList = attendanceRepository.saveAll(attendanceList);


        List<UserAttendanceResponse> attendanceResponseList = savedAttendanceList.stream()
                .map(att -> UserAttendanceResponse.builder()
                        .id(att.getId())
                        .userId(att.getUser().getId())
                        .date(att.getDate()).build())
                .collect(Collectors.toList());

        List<Performance> performanceInfo = userAttendanceRequestList.stream()
                .map(uar -> Performance.builder()
                        .date(now)
                        .user(User.builder().id(uar.getUserId()).build())
                        .km(uar.getKm())
                        .calories(uar.getCalories())
                        .build())
                .collect(Collectors.toList());
        List<Performance> savedPerformanceInfo = performanceRepository.saveAll(performanceInfo);

        List<UserPerformanceInfoResponse> userPerformanceInfoList = savedPerformanceInfo.stream()
                .map(perf -> UserPerformanceInfoResponse.builder()
                        .id(perf.getId())
                        .userId(perf.getUser().getId())
                        .date(perf.getDate())
                        .km(perf.getKm())
                        .calories(perf.getCalories())
                        .build())
                .collect(Collectors.toList());
        return CreateAttendanceResponse.builder()
                .attendanceList(attendanceResponseList)
                .performanceList(userPerformanceInfoList)
                .build();
    }
}
