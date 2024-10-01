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

import java.util.List;

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
    public ResponseEntity<Response<UserIdResponse>> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest){
        UserIdResponse userIdResponse = userService.update(userUpdateRequest);
        return ResponseEntity.ok().body(Response.success(userIdResponse));
    }

    /**
     *  전체 회원(USER) 조회
     *  관리자가 프로젝트 팀을 만들때 필요
     */
    @GetMapping("/admin/all")
    public ResponseEntity<Response<List<UserDetailsResponse>>> getAllUsers(){
        List<UserDetailsResponse> userDetailsResponses = userService.getAllUsers();
        return ResponseEntity.ok().body(Response.success(userDetailsResponses));
    }
}
