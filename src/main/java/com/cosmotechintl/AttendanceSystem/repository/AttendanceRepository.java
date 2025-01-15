package com.cosmotechintl.AttendanceSystem.repository;

import com.cosmotechintl.AttendanceSystem.entity.Attendance;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByUserInfoAndDate(UserInfo user, LocalDate today);

    Optional<Attendance> findByUserInfoAndDate(UserInfo userInfo, LocalDate today);

    Attendance findByUserInfo(UserInfo userinfo);

    @Query(value = "SELECT * FROM attendance WHERE user_id = :userId AND MONTH(date) = :month AND YEAR(date) = :year", nativeQuery = true)
    List<Attendance> findAllByUserIdAndMonthAndYear(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);
}
