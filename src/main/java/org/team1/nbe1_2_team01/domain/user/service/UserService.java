package org.team1.nbe1_2_team01.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.user.controller.dto.UserSignUpDto;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(UserSignUpDto userSignUpDto) {
        if (userRepository.findByUsername(userSignUpDto.username()).isPresent()) {
            throw new IllegalArgumentException(userSignUpDto.username() + "는 이미 존재하는 아이디 입니다.");
        }
        if (userRepository.findByEmail(userSignUpDto.email()).isPresent()) {
            throw new IllegalArgumentException(userSignUpDto.email() + "는 이미 존재하는 이메일 입니다.");
        }
        User user = User.builder()
                .username(userSignUpDto.username())
                .password(userSignUpDto.password())
                .email(userSignUpDto.email())
                .name(userSignUpDto.name())
                .role(Role.USER)
                .build();
        user.passwordEncode(passwordEncoder);
        return userRepository.save(user).getId();
    }
}
