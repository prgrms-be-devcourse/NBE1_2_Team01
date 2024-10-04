package org.team1.nbe1_2_team01.domain.group.service.response;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_2_team01.domain.group.entity.Team;

@Data
public class TeamIdResponse {

    private Long id;

    @Builder
    private TeamIdResponse(Long id) {
        this.id = id;
    }

    public static TeamIdResponse of(Team team) {
        return TeamIdResponse.builder()
                .id(team.getId())
                .build();
    }

}
