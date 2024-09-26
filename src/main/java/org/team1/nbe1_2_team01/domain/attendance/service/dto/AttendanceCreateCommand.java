package org.team1.nbe1_2_team01.domain.attendance.service.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Builder
public record AttendanceCreateCommand(
        User user,
        AttendanceIssueType attendanceIssueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public Attendance toEntity() {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(attendanceIssueType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .build();
    }
}
