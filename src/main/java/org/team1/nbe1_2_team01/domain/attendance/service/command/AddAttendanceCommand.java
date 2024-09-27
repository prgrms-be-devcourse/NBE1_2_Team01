package org.team1.nbe1_2_team01.domain.attendance.service.command;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.service.validation.AttendanceValidator;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Builder
public record AddAttendanceCommand(
        String username,
        AttendanceIssueType attendanceIssueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public AddAttendanceCommand {
        AttendanceValidator.validateAttendTime(startAt, endAt);
    }

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
