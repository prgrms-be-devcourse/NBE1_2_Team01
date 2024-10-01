package org.team1.nbe1_2_team01.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserUpdateRequest;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_2_team01.global.auth.redis.repository.RefreshTokenRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.List;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.*;

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

    @Transactional
    public UserIdResponse update(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.id())
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
        if (userUpdateRequest.name() != null && !userUpdateRequest.name().isEmpty()) {
            user.updateName(userUpdateRequest.name());
        }
        if (userUpdateRequest.password() != null && !userUpdateRequest.password().isEmpty()) {
            user.updatePassword(userUpdateRequest.password());
            user.passwordEncode(passwordEncoder);
        }
        return new UserIdResponse(user.getId());
    }

    public UserDetailsResponse getCurrentUserDetails() {
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));

        return new UserDetailsResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName());
    }

    public List<UserDetailsResponse> getAllUsers() {
        return userRepository.findByRole(Role.USER)
                .stream()
                .map(user -> {
                    return new UserDetailsResponse(user.getId(), user.getUsername(), user.getEmail(), user.getName());
                })
                .toList();
    }

}
