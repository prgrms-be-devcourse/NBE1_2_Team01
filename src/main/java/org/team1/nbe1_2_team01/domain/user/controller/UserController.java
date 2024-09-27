package org.team1.nbe1_2_team01.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.user.controller.dto.UserSignUpDto;
import org.team1.nbe1_2_team01.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Long> signUp(@RequestBody UserSignUpDto userSignUpDto){
        Long id = userService.signUp(userSignUpDto);
        return ResponseEntity.ok().body(id);
    }

}
