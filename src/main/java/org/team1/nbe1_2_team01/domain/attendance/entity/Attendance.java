package org.team1.nbe1_2_team01.domain.attendance.entity;

import jakarta.persistence.*;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Calendar calendar;

//    private User user;

    @Enumerated(value = EnumType.STRING)
    private AttendanceIssueType attendanceIssueType;

    private LocalDate absentDay;

    private String content;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean creationWaiting;

}
