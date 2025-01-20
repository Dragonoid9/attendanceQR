package com.cosmotechintl.AttendanceSystem.repository;

import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query(value = "Select r.name from roles r left join user_role ur ON r.id = ur.role_id " +
            "left join users u on u.id = ur.user_id where u.username=:username", nativeQuery = true)
    List<String> getRolesByUsername(String username);
}
