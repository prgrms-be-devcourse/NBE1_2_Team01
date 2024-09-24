package org.team1.nbe1_2_team01.domain.chat.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParticipantPK implements Serializable {

    private Long userId;
    private Long channelId;

}
