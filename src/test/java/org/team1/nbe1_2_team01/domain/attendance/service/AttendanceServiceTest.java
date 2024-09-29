package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAddAttendanceCommand_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createUpdateAttendanceCommand_ABSENT;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser_onlyId_1L;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    User user;

    @BeforeEach
    void setUp() {
        user = createUser_onlyId_1L();
    }

    @Test
    void 출결_요청_등록() {
        // given
        var createCommand = createAddAttendanceCommand_ABSENT();
        when(attendanceRepository.findByUserIdAndStartAt(user.getId(), LocalDate.now())).thenReturn(Optional.empty());
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        // when
        var createdAttendanceId = attendanceService.registAttendance(user.getId(), createCommand);

        // then
        assertThat(createdAttendanceId).isNotNull();
    }

    @Test
    void 출결_요청_수정() {
        // given
        var updateCommand = createUpdateAttendanceCommand_ABSENT();
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(updateCommand.id())).thenReturn(Optional.of(attendance));

        // when
        attendanceService.updateAttendance(user.getId(), updateCommand);

        // then
        assertThat(attendance.getStartAt()).isEqualTo(updateCommand.startAt());
    }

    @Test
    void 출결_요청_수정_시_출결_요청_데이터가_없다면_예외를_발생시킨다() {
        // given
        var updateCommand = createUpdateAttendanceCommand_ABSENT();
        when(attendanceRepository.findById(updateCommand.id())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> attendanceService.updateAttendance(user.getId(), updateCommand))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 출결_요청_삭제() {
        // given
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(attendance.getId())).thenReturn(Optional.of(attendance));
        doNothing().when(attendanceRepository).delete(attendance);

        // when
        attendanceService.deleteAttendance(user.getId(), attendance.getId());

        // then
        verify(attendanceRepository).delete(attendance);
    }

    @Test
    void 출결_요청_삭제_시_출결_요청_데이터가_없다면_예외를_발생시킨다() {
        // given
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(attendance.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> attendanceService.deleteAttendance(user.getId(), attendance.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 출결_승인() {
        // given
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(attendance.getId())).thenReturn(Optional.of(attendance));

        // when
        attendanceService.approveAttendance(attendance.getId());

        // then
        assertThat(attendance.isCreationWaiting()).isFalse();
    }

    @Test
    void 출결_반려() {
        // given
        var attendanceId = 1L;
        doNothing().when(attendanceRepository).deleteById(attendanceId);

        // when
        attendanceService.rejectAttendance(attendanceId);

        // then
        verify(attendanceRepository).deleteById(attendanceId);
    }
}
