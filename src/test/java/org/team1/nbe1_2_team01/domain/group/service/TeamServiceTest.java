package org.team1.nbe1_2_team01.domain.group.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.team1.nbe1_2_team01.IntegrationTestSupport;
import org.team1.nbe1_2_team01.domain.group.controller.request.TeamCreateRequest;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_2_team01.domain.user.entity.Course;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.fixture.UserFixture;
import org.team1.nbe1_2_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;
import org.team1.nbe1_2_team01.global.util.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
public class TeamServiceTest extends IntegrationTestSupport {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceTest.class);
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    @BeforeEach
    void setup() {
        Course testCourse = Course.builder()
                .name("testCourse")
                .build();
        courseRepository.save(testCourse);
        User admin = UserFixture.createAdmin();
        User user = UserFixture.createUser();
        User user2 = UserFixture.createUser2();

        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(user2);
    }


    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 프로젝트팀_생성_실패_코스_없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(2L, "PROJECT", "projectTeam", List.of(3L), 3L);

        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.COURSE_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void 프로젝트팀_생성_실패_관리자만_가능() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "PROJECT", "projectTeam", List.of(3L), 3L);
        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.NOT_ADMIN_USER);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 프로젝트팀_생성_실패_회원PK_없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "PROJECT", "projectTeam", List.of(3L, 4L, 5L), 3L);

        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 프로젝트팀_생성_성공() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "PROJECT", "projectTeam", List.of(3L, 4L), 3L);
        Message message = teamService.teamCreate(teamCreateRequest);
        assertThat(message.getValue()).isEqualTo("1");
        
    }



}
