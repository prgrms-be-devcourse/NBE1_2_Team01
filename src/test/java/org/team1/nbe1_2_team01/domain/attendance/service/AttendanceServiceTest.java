package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.create_ATTENDANCE_REGISTER;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceRequestFixture.create_ATTENDANCE_CREATE_REQUEST_ABSENT;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceRequestFixture.create_ATTENDANCE_UPDATE_REQUEST_ABSENT;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

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
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;
import org.team1.nbe1_2_team01.domain.common.stub.FixedDateTimeHolder;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private DateTimeHolder dateTimeHolder = new FixedDateTimeHolder(2024, 9, 30, 12, 30);
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
        var createRequest = create_ATTENDANCE_CREATE_REQUEST_ABSENT();
        var attendance = Mockito.spy(createRequest.toEntity(user));
        given(attendanceRepository.findByUserIdAndStartAt(user.getId(), dateTimeHolder.getDate())).willReturn(Optional.empty());
        given(attendanceRepository.save(any(Attendance.class))).willReturn(attendance);
        given(attendance.getId()).willReturn(1L);

        // when
        var createdAttendanceId = attendanceService.registAttendance(user.getUsername(), createRequest);

        // then
        assertThat(createdAttendanceId).isNotNull();
    }

    @Test
    void 출결_요청_등록_시_오늘_이미_등록했다면_예외를_발생시킨다() {
        // given
        var createRequest = create_ATTENDANCE_CREATE_REQUEST_ABSENT();
        var attendance = Mockito.spy(createRequest.toEntity(user));
        given(attendanceRepository.findByUserIdAndStartAt(any(), any())).willReturn(Optional.of(attendance));

        // when & then
        assertThatThrownBy(() -> attendanceService.registAttendance(user.getUsername(), createRequest))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결_요청_수정() {
        // given
        var updateRequest = create_ATTENDANCE_UPDATE_REQUEST_ABSENT();
        var attendance = create_ATTENDANCE_REGISTER(user);
        given(attendanceRepository.findById(any())).willReturn(Optional.of(attendance));

        // when
        attendanceService.updateAttendance(user.getUsername(), updateRequest);

        // then
        assertThat(attendance.getStartAt()).isEqualTo(updateRequest.startAt());
    }

    @Test
    void 출결_요청_삭제() {
        // given
        var attendance = create_ATTENDANCE_ABSENT(user);
        given(attendanceRepository.findById(any())).willReturn(Optional.of(attendance));
        doNothing().when(attendanceRepository).delete(attendance);

        // when
        attendanceService.deleteAttendance(user.getUsername(), attendance.getId());

        // then
        verify(attendanceRepository).delete(attendance);
    }

    // 잠시 보류
    @Test
    void 출결_승인() {
        // given
        var attendance = create_ATTENDANCE_ABSENT(user);
        given(attendanceRepository.findById(any())).willReturn(Optional.of(attendance));

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
