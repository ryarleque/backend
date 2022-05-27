package com.sportlimacenter.service;

import com.sportlimacenter.entity.Performance;
import com.sportlimacenter.entity.User;
import com.sportlimacenter.model.request.UserPerformanceInfoRequest;
import com.sportlimacenter.model.response.CreatePerformanceInfoResponse;
import com.sportlimacenter.model.response.UserPerformanceInfoResponse;
import com.sportlimacenter.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional
    public CreatePerformanceInfoResponse createBulkPerformanceInfo(List<UserPerformanceInfoRequest> userInfoPerformance) {
        LocalDate now = LocalDate.now();
        List<Performance> performanceInfo = userInfoPerformance.stream()
                .map(uip -> Performance.builder()
                        .date(now)
                        .user(User.builder().id(uip.getUserId()).build())
                        .km(uip.getKm())
                        .calories(uip.getCalories())
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
        return CreatePerformanceInfoResponse.builder()
                .performanceInfo(userPerformanceInfoList)
                .build();
    }

}
