package org.team1.nbe1_2_team01.domain.attendance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDate;

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

    private LocalDate absentDay;

    private String content;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean creationWaiting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Builder
    private Attendance(
            User user,
            Calendar calendar,
            AttendanceIssueType attendanceIssueType,
            LocalDate absentDay,
            String content,
            boolean creationWaiting) {
        this.user = user;
        this.calendar = calendar;
        this.attendanceIssueType = attendanceIssueType;
        this.absentDay = absentDay;
        this.content = content;
        this.creationWaiting = creationWaiting;
        user.addAttendance(this);
        calendar.addAttendance(this);
    }

}
