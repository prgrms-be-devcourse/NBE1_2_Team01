package org.team1.nbe1_2_team01.domain.attendance.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.team1.nbe1_2_team01.domain.attendance.fixture.AttendanceFixture.createAttendance;
import static org.team1.nbe1_2_team01.domain.user.fixture.UserFixture.createUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.team1.nbe1_2_team01.domain.attendance.service.port.DateTimeHolder;
import org.team1.nbe1_2_team01.domain.common.stub.FixedDateTimeHolder;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.config.QuerydslConfig;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(AttendanceRepositoryTest.class);

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserRepository userRepository;

    DateTimeHolder dateTimeHolder;

    @BeforeEach
    void setUp() {
        dateTimeHolder = new FixedDateTimeHolder(2024, 9, 30, 0, 0);
    }

    @Test
    void findByUserIdAndStartAtTest() {
        // given
        var user = createUser();
        User savedUser = userRepository.save(user);
        log.info("saved user id: {}", savedUser.getId());
        var attendance = createAttendance(user);
        attendanceRepository.save(attendance);
        log.info("saved attendance id: {}", attendance.getId());

        // when
        var todayAttendance = attendanceRepository.findByUserIdAndStartAt(savedUser.getId(), dateTimeHolder.getDate()).orElseGet(null);
        log.info("today attendance id: {}", todayAttendance.getId());

        // then
        assertThat(todayAttendance.getStartAt().getDayOfMonth()).isEqualTo(dateTimeHolder.getDate().getDayOfMonth());
    }
}
