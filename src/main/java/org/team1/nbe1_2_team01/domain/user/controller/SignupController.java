package org.team1.nbe1_2_team01.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.service.UserService;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_2_team01.global.auth.email.service.EmailService;
import org.team1.nbe1_2_team01.global.util.Response;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class SignupController {
    private final UserService userService;
    private final EmailService emailService;

    /**
     * 이메일 uuid 검증
     */
    @GetMapping("/sign-up/{code}")
    public String verifyUUID(@PathVariable String code, Model model) {
        boolean validCode = emailService.isValidCode(code);
        if (validCode) {
            return "signup";
        }
        model.addAttribute("error", "접근 금지: 인증되지 않은 이메일입니다.");
        return "error";
    }

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public String signUp(@Valid UserSignUpRequest userSignUpRequest, Model model) {
        userService.signUp(userSignUpRequest);
        emailService.deleteByCode(userSignUpRequest.code());
        model.addAttribute("successMessage", "회원가입이 완료되었습니다");
        return "signup";
    }

}