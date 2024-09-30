package org.team1.nbe1_2_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendanceCreateRequest;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createAdmin;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceTest {

    User user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    void 출결_정보_생성() {
        // when
        var attendance = createAttendance(createUser());

        // then
        assertThat(attendance).isNotNull();
    }

    @Test
    void 출결_정보를_생성할_때_출결_시작_시간이_끝_시간보다_나중이면_예외를_발생시킨다() {
        var createRequest = createAttendanceCreateRequest(9, 30, 9, 10);

        assertThatThrownBy(() -> createRequest.toEntity(user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출결_정보를_생성할_때_출결_시간_모두_9시부터_18시_사이가_아니라면_예외를_발생시킨다() {
        assertSoftly(softly -> {
            assertThatThrownBy(() -> {
                var createRequest = createAttendanceCreateRequest(8, 59, 9, 30);
                createRequest.toEntity(user);
            }).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> {
                var createRequest = createAttendanceCreateRequest(9, 10, 18, 30);
                createRequest.toEntity(user);
            }).isInstanceOf(IllegalArgumentException.class);
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
        var attendance = createAttendance(createAdmin());
        attendance.approve();

        // when & then
        assertThatThrownBy(() -> attendance.approve())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
