package org.team1.nbe1_2_team01.domain.attendance.controller.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;

@Builder
public record AttendanceUpdateRequest(
        Long id,
        AttendanceIssueType attendanceIssueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

}
