package org.team1.nbe1_2_team01.domain.attendance.fixture;

import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;
import org.team1.nbe1_2_team01.domain.common.stub.FixedDateTimeHolder;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public class AttendanceFixture {

    // Attendance Fixture
    public static Attendance createAttendance(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(createDateTimeHolder(12, 30).getDateTime())
                .endAt(createDateTimeHolder(15, 0).getDateTime())
                .description("설명")
                .build();
    }

    public static Attendance createAttendance(User user, int startHour, int startMinute, int endHour, int endMinute) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(createDateTimeHolder(startHour, startMinute).getDateTime())
                .endAt(createDateTimeHolder(endHour, endMinute).getDateTime())
                .description("설명")
                .build();
    }

    public static Attendance createAttendance_ABSENT(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(createDateTimeHolder(14, 0).getDateTime())
                .endAt(createDateTimeHolder(16, 0).getDateTime())
                .description("외출사유")
                .build();
    }

    // AttendanceCreateRequest Fixture
    public static AttendanceCreateRequest createAttendanceCreateRequest(int startHour, int startMinute, int endHour, int endMinute) {
        return AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(createDateTimeHolder(startHour, startMinute).getDateTime())
                .endAt(createDateTimeHolder(endHour, endMinute).getDateTime())
                .description("설명")
                .build();
    }

    public static AttendanceCreateRequest createAttendanceCreateRequest_ABSENT() {
        return AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(createDateTimeHolder(14, 0).getDateTime())
                .endAt(createDateTimeHolder(16, 0).getDateTime())
                .description("외출사유")
                .build();
    }

    // AttendanceUpdateRequest Fixture
    public static AttendanceUpdateRequest createAttendanceUpdateRequest_ABSENT() {
        return AttendanceUpdateRequest.builder()
                .id(1L)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(createDateTimeHolder(13, 0).getDateTime())
                .endAt(createDateTimeHolder(16, 0).getDateTime())
                .description("외출사유")
                .build();
    }

    // FixedDateTimeHolder
    public static DateTimeHolder createDateTimeHolder(int hour, int minute) {
        return new FixedDateTimeHolder(2024, 9, 30, hour, minute);
    }
}
