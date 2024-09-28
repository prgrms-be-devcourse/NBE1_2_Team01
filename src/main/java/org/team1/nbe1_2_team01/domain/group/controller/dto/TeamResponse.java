package org.team1.nbe1_2_team01.domain.group.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.entity.Team;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamResponse {

    private Long id;
    private String teamType;
    private String name;
    private boolean creationWaiting;
    private boolean deletionWaiting;
    private List<BelongingResponse> belongings;

    @Builder
    public TeamResponse(Long id, String teamType, String name, boolean creationWaiting, boolean deletionWaiting) {
        this.id = id;
        this.teamType = teamType;
        this.name = name;
        this.creationWaiting = creationWaiting;
        this.deletionWaiting = deletionWaiting;
        this.belongings = new ArrayList<>();
    }

    public static TeamResponse of(Team team) {
        TeamResponse teamResponse = TeamResponse.builder()
                .id(team.getId())
                .teamType(team.getTeamType().name())
                .name(team.getName())
                .creationWaiting(team.isCreationWaiting())
                .deletionWaiting(team.isDeletionWaiting())
                .build();

        for (Belonging b : team.getBelongings()) {
            teamResponse.belongings.add(BelongingResponse.of(b));
        }

        return teamResponse;
    }
}