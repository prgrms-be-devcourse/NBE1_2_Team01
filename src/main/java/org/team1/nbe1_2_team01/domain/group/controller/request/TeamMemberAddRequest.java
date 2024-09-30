package org.team1.nbe1_2_team01.domain.group.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class TeamMemberAddRequest {

    private Long teamId;
    List<Long> userIds;

}
