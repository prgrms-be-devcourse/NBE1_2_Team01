package org.team1.nbe1_2_team01.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.team1.nbe1_2_team01.global.auth.email.service.EmailService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SignupPageController {
    private final EmailService emailService;

    /**
     * 이메일 uuid 검증
     */
    @GetMapping("/sign-up/{code}")
    public String verifyUUID(@PathVariable String code, Model model) {
        boolean validCode = emailService.isValidCode(code);
        if (validCode) {
            model.addAttribute("email", emailService.findByCode(code).getEmail());
            return "signup";
        }
        model.addAttribute("error", "인증되지 않았거나 이미 회원가입된 이메일 입니다");
        return "error";
    }

}
