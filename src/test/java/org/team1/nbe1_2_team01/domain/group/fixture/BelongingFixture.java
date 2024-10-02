package org.team1.nbe1_2_team01.domain.group.fixture;

import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public class BelongingFixture {

    public static Belonging createBelonging_DEVCOURSE_1_MEMBER(User user, Team team) {
        Belonging belonging = Belonging.builder()
                .user(user)
                .course("devcourse_1th")
                .isOwner(false)
                .build();
        belonging.assignTeam(team);
        return belonging;
    }

    public static Belonging createBelonging_DEVCOURSE_1_OWNER(User user, Team team) {
        Belonging belonging = Belonging.builder()
                .user(user)
                .course("devcourse_1th")
                .isOwner(true)
                .build();
        belonging.assignTeam(team);
        return belonging;
    }

    public static Belonging createBelonging_DEVCOURSE_2_MEMBER(User user, Team team) {
        Belonging belonging = Belonging.builder()
                .user(user)
                .course("devcourse_2th")
                .isOwner(false)
                .build();
        belonging.assignTeam(team);
        return belonging;
    }

    public static Belonging createBelonging_COURSE() {
        return Belonging.builder()
                .course("devcourse_1th")
                .isOwner(false)
                .build();
    }
}
