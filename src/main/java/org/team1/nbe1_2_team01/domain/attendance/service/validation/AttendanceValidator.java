package org.team1.nbe1_2_team01.domain.attendance.service.validation;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_TIME_END_BEFORE_START;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_TIME_OUT_OF_RANGE;

import java.time.LocalDateTime;
import org.team1.nbe1_2_team01.global.exception.AppException;

public class AttendanceValidator {

    public static void validateAttendTime(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.getHour() < 9 || startAt.getHour() >= 18
                || endAt.getHour() < 9 || endAt.getHour() >= 18) {
            throw new AppException(ATTENDANCE_TIME_OUT_OF_RANGE);
        }

        if (startAt.isAfter(endAt)) {
            throw new AppException(ATTENDANCE_TIME_END_BEFORE_START);
        }
    }
}
