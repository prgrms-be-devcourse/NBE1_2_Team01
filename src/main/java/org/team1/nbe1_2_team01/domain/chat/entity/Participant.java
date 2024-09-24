package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "participant")
    private List<Chat> chats = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isCreator;

    private LocalDateTime participatedAt;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isParticipated;

    @Builder
    private Participant(User user,
                       Channel channel,
                       boolean isCreator,
                       LocalDateTime participatedAt,
                       boolean isParticipated) {
        this.user = user;
        this.channel = channel;
        this.isCreator = isCreator;
        this.participatedAt = participatedAt;
        this.isParticipated = isParticipated;
        user.addParticipant(this);
        channel.addParticipant(this);
    }

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }
}
