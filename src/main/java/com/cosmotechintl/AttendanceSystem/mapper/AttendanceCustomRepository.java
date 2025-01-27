package com.cosmotechintl.AttendanceSystem.mapper;

import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AttendanceCustomRepository {

    @Select("<script>" +
            "SELECT a.id, a.user_id AS userId, a.checkin, a.checkout, a.date, a.work_type AS workType, u.username " +
            "FROM Attendance a " +
            "JOIN users u ON a.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='user_id != null'>" +
            " AND a.user_id = #{user_id} " +
            "</if>" +
            "<if test='month != null'>" +
            " AND MONTH(a.date) = #{month} " +
            "</if>" +
            "<if test='year != null'>" +
            " AND YEAR(a.date) = #{year} " +
            "</if>" +
            "<if test='work_type != null and work_type != \"\"'>" +
            " AND a.work_type = #{work_type} " +
            "</if>" +
            "<if test='sortBy != null and sortDirection != null'>" +
            " ORDER BY ${sortBy} ${sortDirection} " +
            "</if>" +
            "LIMIT #{size} OFFSET #{offset} " +
            "</script>")
    List<AttendanceResponseDto> findAttendanceByCriteria(@Param("user_id") Long user_id,
                                                         @Param("month") Integer month,
                                                         @Param("year") Integer year,
                                                         @Param("work_type") String work_type,
                                                         @Param("sortBy") String sortBy,
                                                         @Param("sortDirection") String sortDirection,
                                                         @Param("size") Integer size,
                                                         @Param("offset") Integer offset);


    @Select("<script>" +
            "SELECT COUNT(*) FROM Attendance a " +
            "WHERE 1=1 " +
            "<if test='user_id != null'>" +
            " AND a.user_id = #{user_id} " +
            "</if>" +
            "<if test='month != null'>" +
            " AND MONTH(a.date) = #{month} " +
            "</if>" +
            "<if test='year != null'>" +
            " AND YEAR(a.date) = #{year} " +
            "</if>" +
            "<if test='work_type != null and work_type != \"\"'>" +
            " AND a.work_type = #{work_type} " +
            "</if>" +
            "</script>")
    long countAttendanceByCriteria(@Param("user_id") Long user_id,
                                   @Param("month") Integer month,
                                   @Param("year") Integer year,
                                   @Param("work_type") String work_type);


    @Select("<script>" +
            "SELECT   u.username As username,a.checkin, a.checkout, a.date, a.work_type AS workType " +
            "FROM Attendance a " +
            "JOIN users u ON a.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='username != null and username != \"\"'>" +
            " AND u.username = #{username} " +
            "</if>" +
            "<if test='month != null'>" +
            " AND MONTH(a.date) = #{month} " +
            "</if>" +
            "<if test='year != null'>" +
            " AND YEAR(a.date) = #{year} " +
            "</if>" +
            "<if test='work_type != null and work_type != \"\"'>" +
            " AND a.work_type = #{work_type} " +
            "</if>" +
            "<if test='sortBy != null and sortDirection != null'>" +
            " ORDER BY ${sortBy} ${sortDirection} " +
            "</if>" +
            "</script>")
    List<AttendanceResponseDto> findAttendanceByCriteriaExcel(@Param("username") String username,
                                                              @Param("month") Integer month,
                                                              @Param("year") Integer year,
                                                              @Param("work_type") String work_type,
                                                              @Param("sortBy") String sortBy,
                                                              @Param("sortDirection") String sortDirection);
}
