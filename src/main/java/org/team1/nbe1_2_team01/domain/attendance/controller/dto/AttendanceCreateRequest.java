package org.team1.nbe1_2_team01.domain.attendance.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.service.command.AddAttendanceCommand;

public record AttendanceCreateRequest(
        @NotNull(message = "출결 상태를 선택 해야 합니다.")
        AttendanceIssueType attendanceIssueType,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startAt,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endAt,
        String description
) {

    public AddAttendanceCommand toCommand(String username) {
        return AddAttendanceCommand.builder()
                .username(username)
                .attendanceIssueType(attendanceIssueType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .build();
    }
}
