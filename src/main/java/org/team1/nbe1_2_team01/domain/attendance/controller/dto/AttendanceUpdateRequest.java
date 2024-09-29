package org.team1.nbe1_2_team01.domain.attendance.controller.dto;

import java.time.LocalDateTime;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.service.command.UpdateAttendanceCommand;

public record AttendanceUpdateRequest(
        AttendanceIssueType attendanceIssueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public UpdateAttendanceCommand toCommand(Long id) {
        return UpdateAttendanceCommand.builder()
                .id(id)
                .attendanceIssueType(attendanceIssueType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .build();
    }
}
