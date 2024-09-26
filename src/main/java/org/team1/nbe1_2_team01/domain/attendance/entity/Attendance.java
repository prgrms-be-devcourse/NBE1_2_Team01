package org.team1.nbe1_2_team01.domain.attendance.entity;

import jakarta.persistence.Column;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Entity
@Getter
@Table(name = "attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttendanceIssueType attendanceIssueType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String description;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean creationWaiting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Attendance(
            User user,
            AttendanceIssueType attendanceIssueType,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String description) {
        validateAttendTime(startAt, endAt);

        this.user = user;
        this.attendanceIssueType = attendanceIssueType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        this.creationWaiting = true;
        user.addAttendance(this);
    }

    private void validateAttendTime(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.getHour() < 9 || startAt.getHour() >= 18
            || endAt.getHour() < 9 || endAt.getHour() >= 18) {
            throw new IllegalArgumentException("출결 이슈는 9시에서 17시 59분 사이여야 합니다.");
        }

        if (startAt.isAfter(endAt)) {
            throw new IllegalArgumentException("출결 시작 시간이 끝 시간보다 나중일 수 없습니다.");
        }
    }

    public void approve() {
        if (!creationWaiting) {
            throw new IllegalArgumentException("이미 승인되었습니다.");
        }
        creationWaiting = false;
    }
}
