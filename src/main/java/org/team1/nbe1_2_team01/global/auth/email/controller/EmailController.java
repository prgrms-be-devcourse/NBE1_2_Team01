package org.team1.nbe1_2_team01.global.auth.email.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.global.auth.email.controller.request.EmailsRequest;
import org.team1.nbe1_2_team01.global.auth.email.service.EmailService;
import org.team1.nbe1_2_team01.global.util.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/admin")
    public ResponseEntity<Response<String>> registerEmails(@Valid @RequestBody EmailsRequest emailsRequest) throws MessagingException {
        emailService.sendSignUpLinkToEmails(emailsRequest);
        return ResponseEntity.ok().body(Response.success("메일 전송 완료"));
    }

}
