package org.team1.nbe1_2_team01.global.auth.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.global.auth.email.controller.request.EmailsRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    private static final String CONTENT = "<p>회원가입을 하려면 <a href=\"http://localhost:3000\">여기</a>를 클릭하세요</p>";
    private final JavaMailSenderImpl mailSender;

    @Async
    public void sendSignUpLinkToEmails(EmailsRequest emailsRequest) throws MessagingException {
       for(String email : emailsRequest.emails()){
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
           helper.setTo(email);
           helper.setSubject("데브코스 회원가입 링크");
           // true 로 설정 하면 html 로 전송
           helper.setText(CONTENT, true);
           this.javaMailSender.send(message);
           log.info("메일 전송 성공!");
       }
    }

    /**
     *
     */
}
