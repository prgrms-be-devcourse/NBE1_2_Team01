package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.team1.nbe1_2_team01.domain.calendar.fixture.ScheduleFixture.createCalendar;
import static org.team1.nbe1_2_team01.domain.calendar.fixture.ScheduleFixture.createSchedule_MEETING;
import static org.team1.nbe1_2_team01.domain.calendar.fixture.ScheduleFixture.createSchedule_RBF;
import static org.team1.nbe1_2_team01.domain.group.fixture.BelongingFixture.createBelonging_DEVCOURSE_1_MEMBER;
import static org.team1.nbe1_2_team01.domain.group.fixture.TeamFixture.createTeam_PRODUCT_TEAM_1;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.service.GroupAuthService;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private GroupAuthService groupAuthService;

    @InjectMocks
    private ScheduleQueryService scheduleQueryService;

    User user;

    Belonging belonging;

    @BeforeEach
    void setUp() {
        user = Mockito.spy(createUser());
        belonging = Mockito.spy(createBelonging_DEVCOURSE_1_MEMBER(user, createTeam_PRODUCT_TEAM_1()));
        lenient().when(belonging.getId()).thenReturn(1L);
    }

    @Test
    void 공지_일정_조회() {
        // given
        var belongingCourseId = belonging.getId();
        var calendar = createCalendar(belonging);
        var schedule_1 = createSchedule_MEETING(calendar);
        var schedule_2 = createSchedule_RBF(calendar);
        given(scheduleRepository.findByBelongingId(1L)).willReturn(List.of(schedule_1, schedule_2));

        // when
        var noticeScheduleResponses = scheduleQueryService.getNoticeSchedules(belongingCourseId);

        // then
        assertThat(noticeScheduleResponses).hasSize(2);
    }

    @Test
    void 팀_일정_조회() {
        // given
        var belongingTeamId = belonging.getId();
        var calendar = createCalendar(belonging);
        var schedule_1 = createSchedule_MEETING(calendar);
        var schedule_2 = createSchedule_RBF(calendar);
        given(scheduleRepository.findByBelongingId(1L)).willReturn(List.of(schedule_1, schedule_2));

        // when
        var teamScheduleResponses = scheduleQueryService.getTeamSchedules(belongingTeamId);

        // then
        assertThat(teamScheduleResponses).hasSize(2);
    }
}
