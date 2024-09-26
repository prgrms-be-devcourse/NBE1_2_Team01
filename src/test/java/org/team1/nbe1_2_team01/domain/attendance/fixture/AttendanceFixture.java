package org.team1.nbe1_2_team01.domain.attendance.fixture;

import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.service.dto.AttendanceCreateCommand;
import org.team1.nbe1_2_team01.domain.common.stub.FixedDateTimeHolder;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceFixture {

    public static Attendance 출결_생성(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(new FixedDateTimeHolder(12, 30).getDate())
                .endAt(new FixedDateTimeHolder(15, 0).getDate())
                .description("설명")
                .build();
    }

    public static Attendance 출결_생성(User user, int startHour, int startMinute, int endHour, int endMinute) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.LATE)
                .startAt(new FixedDateTimeHolder(startHour, startMinute).getDate())
                .endAt(new FixedDateTimeHolder(endHour, endMinute).getDate())
                .description("설명")
                .build();
    }

    public static AttendanceCreateCommand 외출_요청_등록_명령_생성(User user) {
        return AttendanceCreateCommand.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(new FixedDateTimeHolder(14, 0).getDate())
                .endAt(new FixedDateTimeHolder(16, 0).getDate())
                .description("외출사유")
                .build();
    }

    public static Attendance 출결_생성_외출(User user) {
        return Attendance.builder()
                .user(user)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(new FixedDateTimeHolder(12, 30).getDate())
                .endAt(new FixedDateTimeHolder(15, 0).getDate())
                .description("외출사유")
                .build();
    }
}
