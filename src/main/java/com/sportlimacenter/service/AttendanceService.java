package com.sportlimacenter.service;

import com.sportlimacenter.entity.Attendance;
import com.sportlimacenter.entity.User;
import com.sportlimacenter.model.request.UserAttendanceRequest;
import com.sportlimacenter.model.response.CreateAttendanceResponse;
import com.sportlimacenter.model.response.UserAttendanceResponse;
import com.sportlimacenter.repository.AttendanceRepository;
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

    @Transactional
    public CreateAttendanceResponse createBulkAttendance(List<Long> userIds) {
        LocalDate now = LocalDate.now();
        List<Attendance> attendanceList = userIds.stream()
                .map(userId -> Attendance.builder()
                        .date(now)
                        .user(User.builder().id(userId).build())
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
        return CreateAttendanceResponse.builder()
                .attendance(attendanceResponseList)
                .build();
    }
}
