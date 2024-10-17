package org.team1.nbe1_2_team01.domain.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {

    // GET
    List<Attendance> findByUserId(Long userId);

    Optional<Attendance> findByIdAndUserId(Long attendanceId, Long userId);

    // 자신의 출결 요청이 이미 있는지 검증하기 위한 쿼리
    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND DATE(a.startAt) = :startAt")
    Optional<Attendance> findByUserIdAndStartAt(@Param("userId") Long userId, @Param("startAt") LocalDate startAt);

    Optional<Attendance> findByUserIdAndCreationWaiting(Long userId, boolean creationWaiting);
}
