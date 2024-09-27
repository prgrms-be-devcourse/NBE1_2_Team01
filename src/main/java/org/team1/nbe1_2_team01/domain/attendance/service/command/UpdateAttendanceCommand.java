package org.team1.nbe1_2_team01.domain.attendance.service.command;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.service.validation.AttendanceValidator;

@Builder
public record UpdateAttendanceCommand(
        Long id,
        String username,
        AttendanceIssueType attendanceIssueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public UpdateAttendanceCommand {
        AttendanceValidator.validateAttendTime(startAt, endAt);
    }
}
