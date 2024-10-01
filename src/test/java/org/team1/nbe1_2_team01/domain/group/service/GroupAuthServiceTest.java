package org.team1.nbe1_2_team01.domain.group.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.group.fixture.BelongingFixture.createBelonging_DEVCOURCE_1_MEMBER;
import static org.team1.nbe1_2_team01.domain.group.fixture.TeamFixture.createTeam_PRODUCT_TEAM_1;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class GroupAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BelongingRepository belongingRepository;

    @InjectMocks
    private GroupAuthService groupAuthService;

    User user;

    @BeforeEach
    void setUp() {
        user = Mockito.spy(createUser());
        lenient().when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        lenient().when(user.getId()).thenReturn(1L);
    }

    @Test
    void 코스_소속_검증() {
        // given
        var belonging = Mockito.spy(createBelonging_DEVCOURCE_1_MEMBER(user, createTeam_PRODUCT_TEAM_1()));
        when(belonging.getId()).thenReturn(1L);
        var userIdsAsDEVCOURCE_1 = List.of(1L, 2L, 3L);
        given(belongingRepository.findDistinctUserIdsByCourse(belonging.getCourse())).willReturn(userIdsAsDEVCOURCE_1);

        // when
        Long validatedCourseBelongingId = groupAuthService.validateCourse(belonging.getCourse(), user.getUsername());

        // then
        Assertions.assertThat(validatedCourseBelongingId).isEqualTo(belonging.getId());
    }

    @Test
    void 유저가_코스에_소속되어_있지_않다면_예외를_발생시킨다() {
        // given
        var course = "devcourse_1th";
        var userIdsAsDEVCOURCE_1 = List.of(2L, 3L);
        given(belongingRepository.findDistinctUserIdsByCourse(course)).willReturn(userIdsAsDEVCOURCE_1);

        // when & then
        assertThatThrownBy(() -> groupAuthService.validateCourse(course, user.getUsername()))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 팀_소속_검증() {
        // given
        var belonging = Mockito.spy(createBelonging_DEVCOURCE_1_MEMBER(user, createTeam_PRODUCT_TEAM_1()));
        when(belonging.getId()).thenReturn(1L);
        given(belongingRepository.findById(belonging.getId())).willReturn(Optional.of(belonging));

        // when
        var groupAuthResponse = groupAuthService.validateTeam(user.getUsername(), belonging.getId());

        // then
        Assertions.assertThat(groupAuthResponse.belongingId()).isEqualTo(belonging.getId());
    }

    @Test
    void 유저가_팀에_소속되어_있지_않다면_예외를_발생시킨다() {
        // given
        var anotherUser = Mockito.spy(createUser());
        when(anotherUser.getId()).thenReturn(2L);
        given(userRepository.findByUsername(anotherUser.getUsername())).willReturn(Optional.of(anotherUser));
        var belonging = Mockito.spy(createBelonging_DEVCOURCE_1_MEMBER(user, createTeam_PRODUCT_TEAM_1()));
        when(belonging.getId()).thenReturn(1L);
        given(belongingRepository.findById(belonging.getId())).willReturn(Optional.of(belonging));

        // when & then
        assertThatThrownBy(() -> groupAuthService.validateTeam(anotherUser.getUsername(), belonging.getId()))
                .isInstanceOf(AppException.class);
    }
}
