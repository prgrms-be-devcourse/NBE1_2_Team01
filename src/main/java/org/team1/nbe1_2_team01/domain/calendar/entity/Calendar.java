package org.team1.nbe1_2_team01.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belonging_id")
    private Belonging belonging;

    @OneToMany(mappedBy = "calendar")
    private List<Schedule> schedules = new ArrayList<>();

    @Builder
    private Calendar(Belonging belonging) {
        this.belonging = belonging;
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
    }

    public static Calendar createCalendarOf(Belonging belonging) {
        return Calendar.builder()
                .belonging(belonging)
                .build();
    }

}
