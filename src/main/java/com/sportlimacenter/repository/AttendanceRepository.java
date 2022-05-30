package com.sportlimacenter.repository;

import com.sportlimacenter.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByUserId(long userId);
}
