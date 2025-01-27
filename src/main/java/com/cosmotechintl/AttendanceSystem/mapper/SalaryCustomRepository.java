package com.cosmotechintl.AttendanceSystem.mapper;

import com.cosmotechintl.AttendanceSystem.dto.SalaryDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SalaryCustomRepository {

    @Select("<script>" +
            "SELECT u.username, " +
            "       u.salary, " +
            "       DATE_FORMAT(a.date, '%Y-%m') AS date, " +
            "       SUM(TIMESTAMPDIFF(SECOND, a.checkin, a.checkout)) / 3600 AS totalHours " +
            "FROM users u " +
            "JOIN attendance a ON u.id = a.user_id " +
            "WHERE MONTH(a.date) = #{month} " +
            "  AND YEAR(a.date) = #{year} " +
            "<if test='username != null and username != \"\"'>" +
            "   AND u.username = #{username} " +
            "</if>" +
            "<if test='work_type != null and work_type != \"\"'>" +
            "   AND a.work_type = #{work_type} " +
            "</if>" +
            "GROUP BY u.username, u.salary, DATE_FORMAT(a.date, '%Y-%m')" +
            "<if test='sortBy != null and sortDirection != null'>" +
            " ORDER BY ${sortBy} ${sortDirection} " +
            "</if>" +
            "LIMIT #{size} OFFSET #{offset} " +
            "</script>")
    @Results(id = "SalaryDTOResultMap", value = {
            @Result(column = "username", property = "username"),
            @Result(column = "salary", property = "salary"),
            @Result(column = "date", property = "date"),
            @Result(column = "totalHours", property = "totalHours")
    })
    List<SalaryDTO> findAccruedSalaryByMonthAndYear(@Param("username") String username,
                                                    @Param("month") Integer month,
                                                    @Param("year") Integer year,
                                                    @Param("work_type") String work_type,
                                                    @Param("sortBy") String sortBy,
                                                    @Param("sortDirection") String sortDirection,
                                                    @Param("size") Integer size,
                                                    @Param("offset") Integer offset);

    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM (" +
            "   SELECT u.username, " +
            "          u.salary, " +
            "          DATE_FORMAT(a.date, '%Y-%m') AS date, " +
            "          SUM(TIMESTAMPDIFF(SECOND, a.checkin, a.checkout)) / 3600 AS total_hours " +
            "   FROM users u " +
            "   JOIN attendance a ON u.id = a.user_id " +
            "   WHERE MONTH(a.date) = #{month} " +
            "     AND YEAR(a.date) = #{year} " +
            "   <if test='username != null and username != \"\"'>" +
            "      AND u.username = #{username} " +
            "   </if>" +
            "   <if test='work_type != null and work_type != \"\"'>" +
            "      AND a.work_type = #{work_type} " +
            "   </if>" +
            "   GROUP BY u.username, u.salary, DATE_FORMAT(a.date, '%Y-%m')" +
            ") groupedRecords" +
            "</script>")
    Long countAccruedSalaryByMonthAndYear(@Param("username") String username,
                                   @Param("month") Integer month,
                                   @Param("year") Integer year,
                                   @Param("work_type") String work_type);

}
