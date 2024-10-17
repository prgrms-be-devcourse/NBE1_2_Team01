package org.team1.nbe1_2_team01.domain.attendance.entity;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_ACCESS_DENIED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Embeddable
@EqualsAndHashCode
@Getter
public class AttendanceRegistrant {

    @Column(name = "user_id")
    private final Long registrantId;

    public AttendanceRegistrant(Long registrantId) {
        this.registrantId = registrantId;
    }

    public void validateRegister(Long currentUserId) {
        if (!currentUserId.equals(registrantId)) {
            throw new AppException(ATTENDANCE_ACCESS_DENIED);
        }
    }
}
