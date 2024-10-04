package org.team1.nbe1_2_team01.domain.group.service.response;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;

    @Builder
    private UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
