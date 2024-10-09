package org.team1.nbe1_2_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createAdmin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.global.exception.AppException;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceTest {

    @Test
    void 출결을_생성할_때_출결_시간_모두_9시부터_18시_사이가_아니라면_예외를_발생시킨다() {
        assertSoftly(softly -> {
            assertThatThrownBy(() -> {
                Attendance.builder()
                        .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 59, 0)))
                        .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30, 0)))
                        .build();
            }).isInstanceOf(AppException.class);
            assertThatThrownBy(() -> {
                Attendance.builder()
                        .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 10, 0)))
                        .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 30, 0)))
                        .build();
            }).isInstanceOf(AppException.class);
        });
    }

    @Test
    void 관리자는_출결_정보를_승인한다() {
        // given
        var attendance = createAttendance(createAdmin());

        // when
        attendance.approve();

        // then
        assertThat(attendance.isCreationWaiting()).isFalse();
    }

    @Test
    void 출결이_이미_승인되었다면_예외를_발생시킨다() {
        // given
        var attendance = Attendance.builder()
                .creationWaiting(true)
                .build();
        attendance.approve();

        // when & then
        assertThatThrownBy(() -> attendance.approve())
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결을_등록한_사람이_아닐_때() {
        var user_REGISTER = Mockito.spy(User.class);
        when(user_REGISTER.getId()).thenReturn(1L);
        var user_ANOTHER = Mockito.spy(User.class);
        when(user_ANOTHER.getId()).thenReturn(2L);

        var attendance = Attendance.builder()
                .user(user_REGISTER)
                .build();

        assertThatThrownBy(() -> attendance.validateRegister(user_ANOTHER.getId()))
                .isInstanceOf(AppException.class);
    }
}
