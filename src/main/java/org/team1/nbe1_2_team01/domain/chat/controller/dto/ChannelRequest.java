package org.team1.nbe1_2_team01.domain.chat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelRequest {
    private String channelName;
    private Long creatorUserId;
}
