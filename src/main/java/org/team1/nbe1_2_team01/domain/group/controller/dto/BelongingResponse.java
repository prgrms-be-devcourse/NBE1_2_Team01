package org.team1.nbe1_2_team01.domain.group.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

@Data
public class BelongingResponse {

    private Long id;
    private Long userId;
    private Long teamId;

    @Builder
    public BelongingResponse(Long id, Long userId, Long teamId) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
    }

    public static BelongingResponse of(Belonging belonging) {
        return BelongingResponse.builder()
                .id(belonging.getId())
                .userId(belonging.getUser().getId())
                .teamId(belonging.getTeam().getId())
                .build();
    }
}
