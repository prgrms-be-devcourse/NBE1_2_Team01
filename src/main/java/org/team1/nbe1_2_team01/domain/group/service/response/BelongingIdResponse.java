package org.team1.nbe1_2_team01.domain.group.service.response;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

@Data
public class BelongingIdResponse {

    private Long id;

    @Builder
    public BelongingIdResponse(Long id) {
        this.id = id;
    }

    public static BelongingIdResponse of(Belonging belonging) {
        return BelongingIdResponse.builder()
                .id(belonging.getId())
                .build();
    }
}
