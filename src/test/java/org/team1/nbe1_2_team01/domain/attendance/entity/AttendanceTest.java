package org.team1.nbe1_2_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.출결_생성;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.관리자_생성;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.유저_생성;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceTest {

    @Test
    void 출결_정보_생성() {
        // when
        Attendance attendance = 출결_생성(유저_생성());

        // then
        assertThat(attendance).isNotNull();
    }

    @Test
    void 출결_정보를_생성할_때_출결_시작_시간이_끝_시간보다_나중이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> 출결_생성(유저_생성(), 9, 30, 9, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출결_정보를_생성할_때_출결_시간_모두_9시부터_18시_사이가_아니라면_예외를_발생시킨다() {
        assertSoftly(softly -> {
            assertThatThrownBy(() -> 출결_생성(유저_생성(), 8, 59, 9, 30)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> 출결_생성(유저_생성(), 9, 10, 18, 30)).isInstanceOf(IllegalArgumentException.class);
        });
    }

    @Test
    void 관리자는_출결_정보를_승인한다() {
        // given
        Attendance attendance = 출결_생성(관리자_생성());

        // when
        attendance.approve();

        // then
        assertThat(attendance.isCreationWaiting()).isFalse();
    }

    @Test
    void 출결이_이미_승인되었다면_예외를_발생시킨다() {
        // given
        Attendance attendance = 출결_생성(관리자_생성());
        attendance.approve();

        // when & then
        assertThatThrownBy(() -> attendance.approve())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
