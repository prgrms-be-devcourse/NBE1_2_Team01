package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendanceCreateRequest_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendanceUpdateRequest_ABSENT;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_2_team01.domain.attendance.repository.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
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

    User user;

    @BeforeEach
    void setUp() {
        user = Mockito.spy(createUser());
        lenient().when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        lenient().when(user.getId()).thenReturn(1L);
    }

    @Test
    void 출결_요청_등록() {
        // given
        var createRequest = createAttendanceCreateRequest_ABSENT();
        when(attendanceRepository.findByUserIdAndStartAt(user.getId(), LocalDate.now())).thenReturn(Optional.empty());
        var attendance = Mockito.spy(createAttendance_ABSENT(user));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);
        when(attendance.getId()).thenReturn(1L);

        // when
        var createdAttendanceId = attendanceService.registAttendance(user.getUsername(), createRequest);

        // then
        assertThat(createdAttendanceId).isNotNull();
    }

    @Test
    void 출결_요청_수정() {
        // given
        var updateRequest = createAttendanceUpdateRequest_ABSENT();
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(updateRequest.id())).thenReturn(Optional.of(attendance));

        // when
        attendanceService.updateAttendance(user.getUsername(), updateRequest);

        // then
        assertThat(attendance.getStartAt()).isEqualTo(updateRequest.startAt());
    }

    @Test
    void 출결_요청_수정_시_출결_요청_데이터가_없다면_예외를_발생시킨다() {
        // given
        var updateRequest = createAttendanceUpdateRequest_ABSENT();
        when(attendanceRepository.findById(updateRequest.id())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> attendanceService.updateAttendance(user.getUsername(), updateRequest))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 출결_요청_삭제() {
        // given
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(attendance.getId())).thenReturn(Optional.of(attendance));
        doNothing().when(attendanceRepository).delete(attendance);

        // when
        attendanceService.deleteAttendance(user.getUsername(), attendance.getId());

        // then
        verify(attendanceRepository).delete(attendance);
    }

    @Test
    void 출결_요청_삭제_시_출결_요청_데이터가_없다면_예외를_발생시킨다() {
        // given
        var attendance = createAttendance_ABSENT(user);
        when(attendanceRepository.findById(attendance.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> attendanceService.deleteAttendance(user.getUsername(), attendance.getId()))
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
