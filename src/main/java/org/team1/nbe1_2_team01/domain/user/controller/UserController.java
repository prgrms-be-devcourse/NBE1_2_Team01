package org.team1.nbe1_2_team01.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.service.UserService;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Response<UserIdResponse>> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        UserIdResponse userIdResponse = userService.signUp(userSignUpRequest);
        return ResponseEntity.ok().body(Response.success(userIdResponse));
    }

}
