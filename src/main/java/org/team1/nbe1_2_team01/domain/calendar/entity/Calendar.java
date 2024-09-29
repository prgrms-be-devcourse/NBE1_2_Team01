package org.team1.nbe1_2_team01.domain.calendar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

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

    public Calendar(Long id) {
        this.id = id;
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
