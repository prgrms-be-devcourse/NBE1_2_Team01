package org.team1.nbe1_2_team01.domain.attendance.entity;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_ACCESS_DENIED;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.ATTENDANCE_TIME_OUT_OF_RANGE;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.REQUEST_ALREADY_APPROVED;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.REQUEST_ALREADY_EXISTS;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;

@Entity
@Getter
@Table(name = "attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AttendanceIssueType attendanceIssueType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String description;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean creationWaiting;

    @Embedded
    private AttendanceRegistrant registrant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*@Builder
    private Attendance(
            User user,
            AttendanceIssueType attendanceIssueType,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String description,
            boolean creationWaiting
    ) {
        validateTime(startAt);
        validateTime(endAt);

        this.user = user;
        this.attendanceIssueType = attendanceIssueType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        this.creationWaiting = creationWaiting;
        //user.addAttendance(this);
    }*/

    @Builder
    public Attendance(
            AttendanceIssueType attendanceIssueType,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String description,
            boolean creationWaiting,
            Long registrantId
    ) {
        validateTime(startAt);
        validateTime(endAt);

        this.attendanceIssueType = attendanceIssueType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        this.creationWaiting = creationWaiting;
        this.registrant = new AttendanceRegistrant(registrantId);
    }

    public void update(AttendanceUpdateRequest attendanceUpdateRequest) {
        validateTime(attendanceUpdateRequest.startAt());
        validateTime(attendanceUpdateRequest.endAt());

        this.attendanceIssueType = attendanceUpdateRequest.attendanceIssueType();
        this.startAt = attendanceUpdateRequest.startAt();
        this.endAt = attendanceUpdateRequest.endAt();
        this.description = attendanceUpdateRequest.description();
    }

    public void approve() {
        if (isApprove()) {
            throw new AppException(REQUEST_ALREADY_APPROVED);
        }
        creationWaiting = false;
    }

    public void validateRegistrant(Long currentUserId) {
        Long registerId = user.getId();
        if (!registerId.equals(currentUserId)) {
            throw new AppException(ATTENDANCE_ACCESS_DENIED);
        }
    }

    private void validateTime(LocalDateTime time) {
        LocalTime date = time.toLocalTime();

        if (date.isBefore(LocalTime.of(9, 0, 0))
                || date.isAfter(LocalTime.of(18, 0, 1))) {
            throw new AppException(ATTENDANCE_TIME_OUT_OF_RANGE);
        }
    }

    public void validateCanRegister() {
        if (isApprove()) {
            throw new AppException(REQUEST_ALREADY_APPROVED);
        }
        if (isRegisteredToday()) {
            throw new AppException(REQUEST_ALREADY_EXISTS);
        }
    }

    private boolean isApprove() {
        return !creationWaiting;
    }

    private boolean isRegisteredToday() {
        LocalDate startAtLocalDate = startAt.toLocalDate();
        LocalDate endAtLocalDate = endAt.toLocalDate();
        return startAtLocalDate.isEqual(LocalDate.now()) || endAtLocalDate.isEqual(LocalDate.now());
    }
}
