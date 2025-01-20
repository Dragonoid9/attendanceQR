package com.cosmotechintl.AttendanceSystem.repository;

import com.cosmotechintl.AttendanceSystem.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    @Query(value = "Select access_token from auth_token where access_token= :accessToken AND is_active = false ", nativeQuery = true)
    Optional<String> existByAccessTokenAndIsActiveFalse(String accessToken);

    @Query(value = "Select refresh_token from auth_token where refresh_token= :refreshToken AND is_active = false ", nativeQuery = true)
    Optional<String> existByRefreshTokenAndIsActiveFalse(String refreshToken);

    @Modifying
    @Transactional
    @Query(value = "UPDATE auth_token set is_active = true WHERE refresh_token = :refreshToken", nativeQuery = true)
    void setIsActiveTrue(String refreshToken);

    Optional<AuthToken> findByAccessToken(String accessToken);
}
