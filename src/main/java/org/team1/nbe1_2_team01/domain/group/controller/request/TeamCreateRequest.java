package org.team1.nbe1_2_team01.domain.group.controller.request;

import lombok.Data;
import org.team1.nbe1_2_team01.domain.group.entity.Team;
import org.team1.nbe1_2_team01.domain.group.entity.TeamType;

import java.util.List;

@Data
public class TeamCreateRequest {
    //TODO: 리퀘스트들 Validation 넣기

    private String course;

    private String teamType;

    private String name;

    private List<Long> userIds;

    private Long leaderId;

    public Team toTeamEntity() {
        return Team.builder()
                .teamType(TeamType.valueOf(this.getTeamType()))
                .name(this.getName())
                .creationWaiting(false)
                .deletionWaiting(false)
                .build();
    }

}
