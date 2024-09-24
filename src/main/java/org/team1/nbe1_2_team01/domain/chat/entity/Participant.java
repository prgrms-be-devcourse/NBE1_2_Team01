package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@IdClass(ParticipantPK.class)
public class Participant {

    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Id
    @Column(name = "CHANNEL_ID")
    private Long channelId;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isCreator;

    private LocalDateTime participatedAt;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isParticipated;

}
