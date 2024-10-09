package org.team1.nbe1_2_team01.domain.attendance.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;

public final class AttendanceRequestFixture {

    public static AttendanceCreateRequest create_ATTENDANCE_CREATE_REQUEST_ABSENT() {
        return AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();
    }

    public static AttendanceUpdateRequest create_ATTENDANCE_UPDATE_REQUEST_ABSENT() {
        return AttendanceUpdateRequest.builder()
                .id(1L)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();
    }

}
