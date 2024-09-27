package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
public class ParticipantPK implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "channel_id")
    private Long channelId;

}
