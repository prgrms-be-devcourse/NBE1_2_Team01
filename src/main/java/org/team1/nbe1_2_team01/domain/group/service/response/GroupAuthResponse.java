package org.team1.nbe1_2_team01.domain.group.service.response;

import lombok.Builder;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

@Builder
public record GroupAuthResponse(
        Long belongingId,
        boolean isOwner,
        String course
) {

    public static GroupAuthResponse from(Belonging belonging) {
        return GroupAuthResponse.builder()
                .belongingId(belonging.getId())
                .isOwner(belonging.isOwner())
                .course(belonging.getCourse())
                .build();
    }
}
