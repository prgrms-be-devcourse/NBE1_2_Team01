package org.team1.nbe1_2_team01.domain.attendance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AttendanceIssueType attendanceIssueType;

    private LocalDate absentDay;

    private String content;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean creationWaiting;

}
