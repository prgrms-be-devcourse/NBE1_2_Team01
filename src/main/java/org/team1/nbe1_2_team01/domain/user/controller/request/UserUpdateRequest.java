package org.team1.nbe1_2_team01.domain.user.controller.request;

public record UserUpdateRequest(
        Long id,
        String name,
        String password
) {
}
