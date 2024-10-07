package org.team1.nbe1_2_team01.global.auth.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.global.auth.redis.repository.EmailRepository;
import org.team1.nbe1_2_team01.global.auth.redis.token.EmailToken;
import org.team1.nbe1_2_team01.global.exception.AppException;

import java.util.UUID;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.CODE_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSenderImpl mailSender;
    private final EmailRepository emailRepository;

    private static final String CONTENT = "<p>회원가입을 하려면 <a href=\"%s\">여기</a>를 클릭하세요</p>";
    private static final String SIGNUP_URL = "http://localhost:8080/user/sign-up";
    private static final String CODE = "?code=";
    private static final String COURSE_ID = "&courseId=";

    public void sendSignUpLinkToEmail(String email, Long courseId) throws MessagingException {
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
        helper.setText(createSignupContent(code, courseId), true);
        this.mailSender.send(message);
        log.info("메일 전송 성공");
    }

    private String createSignupContent(UUID uuid, Long courseId) {
        return String.format(CONTENT, SIGNUP_URL + CODE + uuid + COURSE_ID + courseId);
    }


    public boolean isValidCode(String code) {
        boolean exists = emailRepository.existsByCode(code);
        log.info("검증하려는 UUID: {}, 존재 여부: {}", code, exists);
        return exists;
    }

    public EmailToken findByCode(String code) {
        return emailRepository.findByCode(code)
                .orElseThrow(() -> new AppException(CODE_NOT_FOUND));
    }

    public void deleteByEmail(String email) {
        emailRepository.deleteById(email);
    }
}
