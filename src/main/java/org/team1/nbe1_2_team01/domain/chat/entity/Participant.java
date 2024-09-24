package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@IdClass(ParticipantPK.class)
public class Participant {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "channel_id")
    private Long channelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isCreator;

    private LocalDateTime participatedAt;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isParticipated;

}
