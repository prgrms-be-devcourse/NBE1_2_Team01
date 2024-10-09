package org.team1.nbe1_2_team01.domain.attendance.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public class AttendanceFixture {

    public static Attendance create_ATTENDANCE_START_ERR() {
        return Attendance.builder()
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 59, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30, 0)))
                .build();
    }

    public static Attendance create_ATTENDANCE_END_ERR() {
        return Attendance.builder()
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 10, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 30, 0)))
                .build();
    }

    public static Attendance create_ATTENDANCE_NOT_APPROVE() {
        return Attendance.builder()
                .creationWaiting(true)
                .build();
    }

    public static Attendance create_ATTENDANCE_ALREADY_APPROVED() {
        return Attendance.builder()
                .creationWaiting(false)
                .build();
    }

    public static Attendance create_ATTENDANCE_REGISTER(User register) {
        return Attendance.builder()
                .user(register)
                .build();
    }

    public static Attendance create_ATTENDANCE_ABSENT(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
    }
}
