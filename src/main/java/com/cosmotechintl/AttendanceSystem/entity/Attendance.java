package com.cosmotechintl.AttendanceSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id", nullable = false)
    private UserInfo userInfo;

    @Column(name = "checkin")
    private LocalDateTime checkin;

    @Column(name = "checkout")
    private LocalDateTime checkout;

    @Column(name = "date", nullable = false)
    private LocalDate date;

//    @Column(name = "qr_code", nullable = false, length = 255)
//    private String qrCode;
}

