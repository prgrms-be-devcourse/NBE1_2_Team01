package org.team1.nbe1_2_team01.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @OneToMany(mappedBy = "calendar")
    private List<Attendance> attendances = new ArrayList<>();

}
