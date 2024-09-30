package org.team1.nbe1_2_team01.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_2_team01.global.auth.redis.repository.RefreshTokenRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.EMAIL_ALREADY_EXISTS;
import static org.team1.nbe1_2_team01.global.util.ErrorCode.USERNAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserIdResponse signUp(UserSignUpRequest userSignUpRequest) {
        if (userRepository.findByUsername(userSignUpRequest.username()).isPresent()) {
            throw new AppException(USERNAME_ALREADY_EXISTS.withArgs(userSignUpRequest.username()));
        }
        if (userRepository.findByEmail(userSignUpRequest.email()).isPresent()) {
            throw new AppException(EMAIL_ALREADY_EXISTS.withArgs(userSignUpRequest.email()));
        }
        User user = User.builder()
                .username(userSignUpRequest.username())
                .password(userSignUpRequest.password())
                .email(userSignUpRequest.email())
                .name(userSignUpRequest.name())
                .role(Role.USER)
                .build();
        user.passwordEncode(passwordEncoder);
        Long id = userRepository.save(user).getId();
        return new UserIdResponse(id);
    }

    @Transactional
    public void logout() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        refreshTokenRepository.deleteById(currentUsername);
    }
}
