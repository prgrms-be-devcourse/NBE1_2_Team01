package org.team1.nbe1_2_team01.domain.calendar.fixture;

import org.team1.nbe1_2_team01.domain.calendar.entity.Schedule;
import org.team1.nbe1_2_team01.domain.calendar.entity.ScheduleType;
import org.team1.nbe1_2_team01.domain.common.stub.FixedDateTimeHolder;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

public class ScheduleFixture {

    public static Schedule createSchedule_MEETING(Calendar calendar) {
        return Schedule.builder()
                .calendar(calendar)
                .name("team_1_미팅_일정")
                .scheduleType(ScheduleType.MEETING)
                .startAt(new FixedDateTimeHolder(2024, 10, 1, 14, 0).getDateTime())
                .endAt(new FixedDateTimeHolder(2024, 10, 1, 15, 0).getDateTime())
                .description("미팅 일정이 있습니다.")
                .build();
    }

    public static Schedule createSchedule_RBF(Calendar calendar) {
        return Schedule.builder()
                .calendar(calendar)
                .name("team_1_RBF_일정")
                .scheduleType(ScheduleType.MEETING)
                .startAt(new FixedDateTimeHolder(2024, 10, 1, 14, 0).getDateTime())
                .endAt(new FixedDateTimeHolder(2024, 10, 1, 15, 0).getDateTime())
                .description("RBF 일정이 있습니다.")
                .build();
    }

    public static Calendar createCalendar(Belonging belonging) {
        return Calendar.builder()
                .belonging(belonging)
                .build();
    }
}
