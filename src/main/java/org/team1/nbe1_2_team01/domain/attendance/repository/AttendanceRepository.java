package org.team1.nbe1_2_team01.domain.attendance.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByUserIdAndCreationWaitingIsFalse(Long userId);
}
