package org.team1.nbe1_2_team01.domain.attendance.service.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Builder
public record AttendanceResponse(
        Long attendanceId,
        Long userId,
        String username,
        AttendanceIssueType attendanceIssueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {
    public static AttendanceResponse from(User user, Attendance attendance) {
        return AttendanceResponse.builder()
                .attendanceId(attendance.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .attendanceIssueType(attendance.getAttendanceIssueType())
                .startAt(attendance.getStartAt())
                .endAt(attendance.getEndAt())
                .description(attendance.getDescription())
                .build();
    }
}
