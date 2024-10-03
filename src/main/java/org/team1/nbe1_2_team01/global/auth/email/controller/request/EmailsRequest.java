package org.team1.nbe1_2_team01.global.auth.email.controller.request;

import jakarta.validation.constraints.Email;

import java.util.List;

public record EmailsRequest(
        List<@Email(message = "유효하지 않은 이메일 형식입니다.") String> emails
) {
}
