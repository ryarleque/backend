package com.sportlimacenter.service;

import com.sportlimacenter.entity.Attendance;
import com.sportlimacenter.entity.Performance;
import com.sportlimacenter.entity.User;
import com.sportlimacenter.model.request.UserAttendanceRequest;
import com.sportlimacenter.model.response.AttendanceResponse;
import com.sportlimacenter.model.response.CreateAttendanceResponse;
import com.sportlimacenter.model.response.UserAttendanceResponse;
import com.sportlimacenter.model.response.UserPerformanceInfoResponse;
import com.sportlimacenter.repository.AttendanceRepository;
import com.sportlimacenter.repository.PerformanceRepository;
import com.sportlimacenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

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

    public List<AttendanceResponse> getAttendanceByDni(String dni) {
        User user = userRepository.findByEnabledTrueAndDni(dni)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid dni"));
        String campus = user.getBranch().getDescription();
        List<AttendanceResponse> attendanceList = attendanceRepository.findAllByUserId(user.getId())
                .stream()
                .filter(distinctByKey(Attendance::getDate))
                .map(att -> AttendanceResponse.builder()
                        .date(att.getDate())
                        .branch(campus)
                        .attended(true).build())
                .collect(Collectors.toList());

        List<LocalDate> datesAttended = attendanceList.stream()
                .map(AttendanceResponse::getDate)
                .collect(Collectors.toList());
        LocalDate startDate = user.getStartDate().toLocalDate();
        List<LocalDate> datesBetweenStartDateToNow = startDate.datesUntil(LocalDate.now().plusDays(1))
                .collect(Collectors.toList());

        List<AttendanceResponse> missingAttendanceList = datesBetweenStartDateToNow
                .stream()
                .filter(d -> !datesAttended.contains(d))
                .map(d -> AttendanceResponse.builder()
                        .date(d)
                        .branch(campus)
                        .attended(false)
                        .build()).collect(Collectors.toList());
        attendanceList.addAll(missingAttendanceList);

        return attendanceList.stream()
                .sorted(Comparator.comparing(a -> a.getDate()))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
