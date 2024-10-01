package org.team1.nbe1_2_team01.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.calendar.fixture.ScheduleFixture.createCalendar;
import static org.team1.nbe1_2_team01.domain.calendar.fixture.ScheduleFixture.createSchedule_MEETING;
import static org.team1.nbe1_2_team01.domain.calendar.fixture.ScheduleFixture.createSchedule_RBF;
import static org.team1.nbe1_2_team01.domain.group.fixture.BelongingFixture.createBelonging_DEVCOURCE_1_MEMBER;
import static org.team1.nbe1_2_team01.domain.group.fixture.TeamFixture.createTeam_PRODUCT_TEAM_1;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.calendar.repository.ScheduleRepository;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private BelongingRepository belongingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleQueryService scheduleQueryService;

    User user;

    @BeforeEach
    void setUp() {
        user = Mockito.spy(createUser());
        lenient().when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        lenient().when(user.getId()).thenReturn(1L);
    }

    @Test
    void 공지_일정_조회() {
        // given
        var belonging = Mockito.spy(createBelonging_DEVCOURCE_1_MEMBER(user, createTeam_PRODUCT_TEAM_1()));
        when(belonging.getId()).thenReturn(1L);
        var userIdsAsDEVCOURCE_1 = List.of(1L, 2L, 3L);
        given(belongingRepository.findDistinctUserIdsByCourse(belonging.getCourse())).willReturn(userIdsAsDEVCOURCE_1);
        var calendar = createCalendar(belonging);
        var schedule_1 = createSchedule_MEETING(calendar);
        var schedule_2 = createSchedule_RBF(calendar);
        given(scheduleRepository.findByBelongingId(belonging.getId())).willReturn(List.of(schedule_1, schedule_2));

        // when
        var noticeScheduleResponses = scheduleQueryService.getNoticeSchedules(user.getUsername(),
                belonging.getCourse());

        // then
        assertThat(noticeScheduleResponses).hasSize(2);
    }

    @Test
    void 공지_일정_조회_시_유저가_코스에_소속되어_있지_않다면_예외를_발생시킨다() {
        // given
        var course = "devcourse_1th";
        var userIdsAsDEVCOURCE_1 = List.of(2L, 3L);
        given(belongingRepository.findDistinctUserIdsByCourse(course)).willReturn(userIdsAsDEVCOURCE_1);

        // when & then
        assertThatThrownBy(() -> scheduleQueryService.getNoticeSchedules(user.getUsername(), course))
                .isInstanceOf(AppException.class);
    }
}
