package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "channel_id", referencedColumnName = "channel_id")
    })
    private Participant participant;

    private String content;

    private LocalDateTime createdAt;

    @Builder
    private Chat(
            Participant participant,
            String content,
            LocalDateTime createdAt) {
        this.participant = participant;
        this.content = content;
        this.createdAt = createdAt;
        participant.addChat(this);
    }
}
