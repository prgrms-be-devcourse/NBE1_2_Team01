package org.team1.nbe1_2_team01.domain.user.fixture;

import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public class UserFixture {

    public static User createAdmin() {
        return User.builder()
                .username("root")
                .password("1234")
                .email("root@gmail.com")
                .name("홍길동")
                .role(Role.ADMIN)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .username("user")
                .password("1234")
                .email("user@gmail.com")
                .name("김철수")
                .role(Role.USER)
                .build();
    }

    public static User createUser2() {
        return User.builder()
                .username("user2")
                .password("1234")
                .email("user2@gmail.com")
                .name("박상순")
                .role(Role.USER)
                .build();
    }
}
