package com.cosmotechintl.AttendanceSystem.repository;

import com.cosmotechintl.AttendanceSystem.entity.QR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface QRRepository extends JpaRepository<QR, Long> {


    boolean existsByTokenAndDate(String uidtoken, LocalDate today);
}
