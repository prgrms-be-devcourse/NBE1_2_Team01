package org.team1.nbe1_2_team01.domain.group.fixture;

import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.user.entity.User;

public class BelongingFixture {

    public static Belonging createBelonging_DEVCOURCE_1_MEMBER(User user, Team team) {
        Belonging belonging = Belonging.builder()
                .user(user)
                .course("devcource_1th")
                .isOwner(false)
                .build();
        belonging.assignTeam(team);
        return belonging;
    }
}
