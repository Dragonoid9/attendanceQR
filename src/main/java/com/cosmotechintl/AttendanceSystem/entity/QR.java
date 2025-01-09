package com.cosmotechintl.AttendanceSystem.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="qr")
public class QR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "token")
    String token;

    @Column(name ="date")
    LocalDate date;

    @Column(name ="qr_url")
    String qr_url;
}
