package org.team1.nbe1_2_team01.domain.attendance.service.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.IssueType;

@Builder
public record AttendanceResponse(
        Long attendanceId,
        Long userId,
        String username,
        IssueType issueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {
    public static AttendanceResponse from(Long userId, String username, Attendance attendance) {
        return AttendanceResponse.builder()
                .attendanceId(attendance.getId())
                .userId(userId)
                .username(username)
                .issueType(attendance.getIssueType())
                .startAt(attendance.getDuration().getStartAt())
                .endAt(attendance.getDuration().getEndAt())
                .description(attendance.getDescription())
                .build();
    }
}
