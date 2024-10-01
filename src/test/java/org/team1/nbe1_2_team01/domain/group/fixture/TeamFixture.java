package org.team1.nbe1_2_team01.domain.group.fixture;

import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.entity.TeamType;

public class TeamFixture {

    public static Team createTeam_PRODUCT_TEAM_1() {
        return Team.builder()
                .name("TEAM_1")
                .teamType(TeamType.PROJECT)
                .creationWaiting(false)
                .deletionWaiting(false)
                .build();
    }
}
