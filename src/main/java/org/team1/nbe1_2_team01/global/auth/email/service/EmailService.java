package org.team1.nbe1_2_team01.global.auth.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.global.auth.email.controller.request.EmailsRequest;
import org.team1.nbe1_2_team01.global.auth.redis.repository.EmailRepository;
import org.team1.nbe1_2_team01.global.auth.redis.token.EmailToken;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final JavaMailSenderImpl mailSender;
    private final EmailRepository emailRepository;

    private static final String CONTENT = "<p>회원가입을 하려면 <a href=\"%s\">여기</a>를 클릭하세요</p>";
    private static final String SIGNUP_URL = "http://localhost:8080/api/user/sign-up/";

    public void sendSignUpLinkToEmails(String email) throws MessagingException {
           UUID code = UUID.randomUUID();
           EmailToken emailToken = EmailToken.builder()
                   .email(email)
                   .code(String.valueOf(code))
                   .build();
           emailRepository.save(emailToken);
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
           helper.setTo(email);
           helper.setSubject("데브코스 회원가입 링크");
           // true 로 설정 하면 html 로 전송
           helper.setText(createSignupContent(code), true);
           this.javaMailSender.send(message);
           log.info("메일 전송 성공!");
    }

    private String createSignupContent(UUID uuid){
        return String.format(CONTENT, SIGNUP_URL + uuid);
    }

    public boolean isValidCode(String code){
        boolean exists = emailRepository.existsByCode(code);
        log.info("검증하려는 UUID: {}, 존재 여부: {}", code, exists);
        return exists;
    }
}
