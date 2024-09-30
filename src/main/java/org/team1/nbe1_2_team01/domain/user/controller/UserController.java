package org.team1.nbe1_2_team01.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserUpdateRequest;
import org.team1.nbe1_2_team01.domain.user.service.UserService;
import org.team1.nbe1_2_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Response<UserIdResponse>> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        UserIdResponse userIdResponse = userService.signUp(userSignUpRequest);
        return ResponseEntity.ok().body(Response.success(userIdResponse));
    }

    /**
     * 로그 아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        userService.logout();
        return ResponseEntity.noContent().build();
    }

    /**
     * 내 정보 조회하기
     */
    @GetMapping
    public ResponseEntity<Response<UserDetailsResponse>> getDetails(){
        UserDetailsResponse userDetailsResponse = userService.getCurrentUserDetails();
        return ResponseEntity.ok().body(Response.success(userDetailsResponse));
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping
    public ResponseEntity<Response<UserIdResponse>> update(@RequestBody UserUpdateRequest userUpdateRequest){
        UserIdResponse userIdResponse = userService.update(userUpdateRequest);
        return ResponseEntity.ok().body(Response.success(userIdResponse));
    }
}
