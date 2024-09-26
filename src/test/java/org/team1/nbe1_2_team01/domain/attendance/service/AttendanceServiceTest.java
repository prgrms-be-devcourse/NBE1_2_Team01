package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.외출_요청_등록_명령_생성;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.출결_생성_외출;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.유저_생성;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceAdminService attendanceAdminService;

    @Test
    void 출결_요청_등록() {
        // given
        var createCommand = 외출_요청_등록_명령_생성(유저_생성());
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(출결_생성_외출(유저_생성()));

        // when
        var actualAttendance = attendanceService.registAttendance(createCommand);

        // then
        assertThat(actualAttendance).isNotNull();
    }

    @Test
    void 출결_승인() {
        // given
        var attendance = 출결_생성_외출(유저_생성());
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(any());

        // when
        var updatedAttendance = attendanceAdminService.approveAttendance(attendance);

        // then
        assertThat(updatedAttendance.isCreationWaiting()).isFalse();
    }

    @Test
    void 출결_반려() {
        // given
        var attendance = 출결_생성_외출(유저_생성());
        doNothing().when(attendanceRepository).delete(any(Attendance.class));

        // when
        attendanceAdminService.rejectAttendance(attendance);

        // then
        verify(attendanceRepository).delete(any(Attendance.class));
    }
}
