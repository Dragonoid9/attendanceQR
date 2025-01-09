package com.cosmotechintl.AttendanceSystem.repository;

import com.cosmotechintl.AttendanceSystem.entity.Attendance;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserInfoAndDate(UserInfo user, LocalDate today);

    Optional<Attendance> findByUserInfoAndDate(UserInfo userInfo, LocalDate today);

    Attendance findByUserInfo(UserInfo userinfo);
}
