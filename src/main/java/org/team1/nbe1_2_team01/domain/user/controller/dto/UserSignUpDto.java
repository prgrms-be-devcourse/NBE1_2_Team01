package org.team1.nbe1_2_team01.domain.user.controller.dto;

public record UserSignUpDto(
        String username,
        String password,
        String email,
        String name
) {

}
