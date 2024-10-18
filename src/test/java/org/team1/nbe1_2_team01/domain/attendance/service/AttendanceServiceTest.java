package org.team1.nbe1_2_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.create_USER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceCreateRequest;
import org.team1.nbe1_2_team01.domain.attendance.controller.dto.AttendanceUpdateRequest;
import org.team1.nbe1_2_team01.domain.attendance.entity.AttendanceIssueType;
import org.team1.nbe1_2_team01.domain.attendance.fake.AttendanceFakeRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.port.AttendanceRepository;
import org.team1.nbe1_2_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceServiceTest {

    private final AttendanceReader attendanceReader;
    private final AttendanceRegistrar attendanceRegistrar;
    private final AttendanceUpdater attendanceUpdater;
    private final AttendanceDeleter attendanceDeleter;
    @Mock
    private UserRepository userRepository;

    public AttendanceServiceTest() {
        AttendanceRepository attendanceRepository = new AttendanceFakeRepository();
        this.attendanceReader = new AttendanceReader(attendanceRepository);
        this.attendanceRegistrar = new AttendanceRegistrar(attendanceRepository);
        this.attendanceUpdater = new AttendanceUpdater(attendanceRepository);
        this.attendanceDeleter = new AttendanceDeleter(attendanceRepository);
    }

    User user;

    @BeforeEach
    void setUp() {
        user = create_USER();
        // stub
        lenient().when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }

    @Test
    void 출결_요청을_성공적으로_등록한다() {
        // given
        String registrantName = user.getUsername();
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceService attendanceService = new AttendanceService(
                attendanceRegistrar, attendanceReader, null, null, userRepository
        );
        AttendanceIdResponse response = attendanceService.register(registrantName, attendanceCreateRequest);

        // then
        assertThat(response.attendanceId()).isEqualTo(1L);
    }

    @Test
    void 출결_요청_등록_시_오늘_이미_등록했다면_예외를_발생시킨다() {
        // given
        String registrantName = user.getUsername();
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();
        attendanceRegistrar.register(user.getId(), attendanceCreateRequest);

        // when
        AttendanceService attendanceService = new AttendanceService(
                attendanceRegistrar, attendanceReader, null, null, userRepository
        );

        // then
        assertThatThrownBy(() -> attendanceService.register(registrantName, attendanceCreateRequest))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결_요청을_성공적으로_수정한다() {
        // given
        Long registrantId = 1L;
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();
        attendanceRegistrar.register(registrantId, attendanceCreateRequest);

        Long attendanceId = 1L;
        String registrantName = user.getUsername();
        AttendanceUpdateRequest attendanceUpdateRequest = AttendanceUpdateRequest.builder()
                .id(attendanceId)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceService attendanceService = new AttendanceService(
                null, attendanceReader, attendanceUpdater, null, userRepository
        );
        AttendanceIdResponse attendanceIdResponse = attendanceService.update(registrantName, attendanceUpdateRequest);

        // then
        assertThat(attendanceIdResponse.attendanceId()).isEqualTo(attendanceId);
    }

    @Test
    void 출결_요청_수정_시_등록자가_아니라면_예외를_발생시킨다() {
        // given
        Long registrantId = 2L;
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();
        attendanceRegistrar.register(registrantId, attendanceCreateRequest);

        Long attendanceId = 1L;
        String registrantName = user.getUsername();
        AttendanceUpdateRequest attendanceUpdateRequest = AttendanceUpdateRequest.builder()
                .id(attendanceId)
                .attendanceIssueType(AttendanceIssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceService attendanceService = new AttendanceService(
                null, attendanceReader, attendanceUpdater, null, userRepository
        );

        // then
        assertThatThrownBy(() -> attendanceService.update(registrantName, attendanceUpdateRequest))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결_승인() {
    }

    @Test
    void 출결_반려() {
    }
}
