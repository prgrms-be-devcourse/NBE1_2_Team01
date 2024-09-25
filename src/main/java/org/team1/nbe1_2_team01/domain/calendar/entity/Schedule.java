package org.team1.nbe1_2_team01.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private ScheduleType scheduleType;

    private LocalDateTime scheduledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;


    @Builder
    private Schedule(
            Calendar calendar,
            String name,
            ScheduleType scheduleType,
            LocalDateTime scheduledAt) {
        this.calendar = calendar;
        this.name = name;
        this.scheduleType = scheduleType;
        this.scheduledAt = scheduledAt;
        calendar.addSchedule(this);
    }
}
