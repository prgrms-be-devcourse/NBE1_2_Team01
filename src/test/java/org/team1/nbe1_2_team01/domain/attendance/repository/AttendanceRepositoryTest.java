package org.team1.nbe1_2_team01.domain.attendance.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
        attendanceRepository.deleteAll();
    }

    @Test
    void findByUserIdAndStartAtTest() {
        // given
        var user = createUser();
        userRepository.save(user);
        var attendance = createAttendance(user);
        attendanceRepository.save(attendance);

        // when
        var todayAttendance = attendanceRepository.findByUserIdAndStartAt(1L, LocalDate.now()).orElseGet(null);

        // then
        assertThat(todayAttendance.getStartAt().getDayOfMonth()).isEqualTo(LocalDate.now().getDayOfMonth());
    }
}
