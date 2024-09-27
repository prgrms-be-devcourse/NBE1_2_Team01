package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAddAttendanceCommand_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createUpdateAttendanceCommand_ABSENT;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void 출결_요청_등록() {
        // given
        var user = createUser();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        var createCommand = createAddAttendanceCommand_ABSENT(user);
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(createAttendance_ABSENT(user));

        // when
        var actualAttendance = attendanceService.registAttendance(createCommand);

        // then
        assertThat(actualAttendance).isNotNull();
    }

    @Test
    void 출결_요청_수정() {
        // given
        var updateCommand = createUpdateAttendanceCommand_ABSENT();
        var attendance = createAttendance_ABSENT(createUser());
        when(attendanceRepository.findById(any())).thenReturn(Optional.ofNullable(attendance));

        // when
        var updatedAttendance = attendanceService.updateAttendance(updateCommand);

        // then
        assertThat(updatedAttendance.getStartAt()).isEqualTo(updateCommand.startAt());
    }

    @Test
    void 출결_요청_수정_시_출결_요청_데이터가_없다면_예외를_발생시킨다() {
        // given
        var updateCommand = createUpdateAttendanceCommand_ABSENT();
        when(attendanceRepository.findById(updateCommand.id())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> attendanceService.updateAttendance(updateCommand))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 출결_요청_삭제() {
        // given
        var attendanceId = 1L;
        doNothing().when(attendanceRepository).deleteById(attendanceId);

        // when
        attendanceService.deleteAttendance(attendanceId);

        // then
        verify(attendanceRepository).deleteById(attendanceId);
    }

    @Test
    void 출결_요청_삭제_시_출결_요청_데이터가_없다면_예외를_발생시킨다() {
        // given
        var attendanceId = 1L;
        doThrow(EntityNotFoundException.class).when(attendanceRepository).deleteById(attendanceId);

        // when & then
        assertThatThrownBy(() -> attendanceService.deleteAttendance(attendanceId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 출결_승인() {
        // given
        var attendanceId = 1L;
        var attendance = AttendanceFixture.createAttendance(createUser());
        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(any());

        // when
        var updatedAttendance = attendanceService.approveAttendance(attendanceId);

        // then
        assertThat(updatedAttendance.isCreationWaiting()).isFalse();
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
