package org.team1.nbe1_2_team01.domain.attendance.service.validation;

import java.time.LocalDateTime;

public class AttendanceValidator {

    public static void validateAttendTime(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.getHour() < 9 || startAt.getHour() >= 18
                || endAt.getHour() < 9 || endAt.getHour() >= 18) {
            throw new IllegalArgumentException("출결 이슈는 9시에서 17시 59분 사이여야 합니다.");
        }

        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("출결 시작 시간이 끝 시간보다 나중일 수 없습니다.");
        }
    }
}
