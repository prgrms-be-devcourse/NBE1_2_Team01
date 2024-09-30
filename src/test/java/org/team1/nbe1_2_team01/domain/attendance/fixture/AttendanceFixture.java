package org.team1.nbe1_2_team01.domain.attendance.fixture;

import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.common.stub.FixedDateTimeHolder;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public class AttendanceFixture {

    // Attendance Fixture
    public static Attendance createAttendance(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(new FixedDateTimeHolder(12, 30).getDate())
                .endAt(new FixedDateTimeHolder(15, 0).getDate())
                .description("설명")
                .build();
    }

    public static Attendance createAttendance(User user, int startHour, int startMinute, int endHour, int endMinute) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(new FixedDateTimeHolder(startHour, startMinute).getDate())
                .endAt(new FixedDateTimeHolder(endHour, endMinute).getDate())
                .description("설명")
                .build();
    }

    public static Attendance createAttendance_ABSENT(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(new FixedDateTimeHolder(14, 0).getDate())
                .endAt(new FixedDateTimeHolder(16, 0).getDate())
                .description("외출사유")
                .build();
    }

    // AttendanceCreateRequest Fixture
    public static AttendanceCreateRequest createAttendanceCreateRequest(int startHour, int startMinute, int endHour, int endMinute) {
        return AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(new FixedDateTimeHolder(startHour, startMinute).getDate())
                .endAt(new FixedDateTimeHolder(endHour, endMinute).getDate())
                .description("설명")
                .build();
    }

    public static AttendanceCreateRequest createAttendanceCreateRequest_ABSENT() {
        return AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(new FixedDateTimeHolder(14, 0).getDate())
                .endAt(new FixedDateTimeHolder(16, 0).getDate())
                .description("외출사유")
                .build();
    }

    // AttendanceUpdateRequest Fixture
    public static AttendanceUpdateRequest createAttendanceUpdateRequest_ABSENT() {
        return AttendanceUpdateRequest.builder()
                .id(1L)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(new FixedDateTimeHolder(13, 0).getDate())
                .endAt(new FixedDateTimeHolder(16, 0).getDate())
                .description("외출사유")
                .build();
    }
}
