package org.team1.nbe1_2_team01.domain.group.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamMemberAddRequest {

    List<Long> userIds;

}
