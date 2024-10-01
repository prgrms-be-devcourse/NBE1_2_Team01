package org.team1.nbe1_2_team01.domain.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.IntegrationTestSupport;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.auth.redis.repository.RefreshTokenRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    @Test
    void 회원가입_성공() {
        // Given
        UserSignUpRequest request = new UserSignUpRequest("user", "1234abcd", "user@gmail.com", "김철수");

        // When
        userService.signUp(request);

        // Then
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 없음"));
        assertThat(user.getUsername()).isEqualTo(request.username());
        assertThat(passwordEncoder.matches(request.password(), user.getPassword())).isTrue();
        assertThat(user.getEmail()).isEqualTo(request.email());
        assertThat(user.getName()).isEqualTo(request.name());
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void 회원가입_실패_아이디_중복() {
        //given
        UserSignUpRequest request1 = new UserSignUpRequest(
                "user",
                "1234abcd",
                "user@gmail.com",
                "김철수");

        userService.signUp(request1);
        //when
        UserSignUpRequest request2 = new UserSignUpRequest(
                "user",
                "abcd1234",
                "user@naver.com",
                "김영희");
        // then
        assertThat(assertThrows(AppException.class, () -> userService.signUp(request2)).getErrorCode())
                .isEqualTo(ErrorCode.USERNAME_ALREADY_EXISTS);

    }

    @Test
    void 회원가입_실패_이메일_중복() {
        //given
        UserSignUpRequest request1 = new UserSignUpRequest(
                "user",
                "1234abcd",
                "user@gmail.com",
                "김철수");

        userService.signUp(request1);

        //when
        UserSignUpRequest request2 = new UserSignUpRequest(
                "usre",
                "abcd1234",
                "user@gmail.com",
                "김영희");

        // then
        assertThat(assertThrows(AppException.class, () -> userService.signUp(request2)).getErrorCode())
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
    }


}