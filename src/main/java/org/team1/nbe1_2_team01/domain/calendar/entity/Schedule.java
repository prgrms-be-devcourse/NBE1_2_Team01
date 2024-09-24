package org.team1.nbe1_2_team01.domain.calendar.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Calendar calendar;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private ScheduleType scheduleType;

    private LocalDateTime scheduledAt;

}
