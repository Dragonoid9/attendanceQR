package com.cosmotechintl.AttendanceSystem.repository;

import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
