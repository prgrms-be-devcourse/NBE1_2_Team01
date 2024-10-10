package org.team1.nbe1_2_team01.domain.attendance.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.validation.PeriodCheck;

@Builder
@PeriodCheck
public record AttendanceCreateRequest(
        @NotNull(message = "출결 상태를 선택 해야 합니다.")
        AttendanceIssueType attendanceIssueType,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startAt,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endAt,
        String description
) {

    public Attendance toEntity(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(attendanceIssueType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .build();
    }
}
