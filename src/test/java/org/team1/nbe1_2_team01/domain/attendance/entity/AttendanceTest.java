package org.team1.nbe1_2_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_ALREADY_APPROVED;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_END_ERR;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_NOT_APPROVE;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_REGISTER;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_START_ERR;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceTest {

    @Test
    void 출결을_생성할_때_출결_시간_모두_9시부터_18시_사이가_아니라면_예외를_발생시킨다() {
        assertSoftly(softly -> {
            assertThatThrownBy(() -> create_ATTENDANCE_START_ERR())
                    .isInstanceOf(AppException.class);
            assertThatThrownBy(() -> create_ATTENDANCE_END_ERR())
                    .isInstanceOf(AppException.class);
        });
    }

    @Test
    void 출결_정보를_승인한다() {
        // given
        var attendance = create_ATTENDANCE_NOT_APPROVE();

        // when
        attendance.approve();

        // then
        assertThat(attendance.isCreationWaiting()).isFalse();
    }

    @Test
    void 출결이_이미_승인되었다면_예외를_발생시킨다() {
        // given
        var attendance = create_ATTENDANCE_ALREADY_APPROVED();

        // when & then
        assertThatThrownBy(() -> attendance.approve())
                .isInstanceOf(AppException.class);
    }

    // 잠시 보류
    @Test
    void 출결_요청을_등록한_사람인지_검증한다() {
        // given
        var user_REGISTER = Mockito.spy(User.class);
        when(user_REGISTER.getId()).thenReturn(1L);
        var attendance = create_ATTENDANCE_REGISTER(user_REGISTER);

        // when & then
        assertThatThrownBy(() -> attendance.validateRegister(1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 출결_요청을_등록한_사람이_아닐_때_예외를_발생시킨다() {
        // given
        var user_REGISTER = Mockito.spy(User.class);
        when(user_REGISTER.getId()).thenReturn(1L);
        var attendance = create_ATTENDANCE_REGISTER(user_REGISTER);

        // when
        var user_ANOTHER_ID = 2L;

        // then
        assertThatThrownBy(() -> attendance.validateRegister(user_ANOTHER_ID))
                .isInstanceOf(AppException.class);
    }
}
