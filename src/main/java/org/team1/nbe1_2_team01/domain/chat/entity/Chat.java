package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @JoinColumn(name = "channel_id", referencedColumnName = "channel_id")
    })
    private Participant participant;


    @Builder
    private Chat(
            String content,
            LocalDateTime createdAt,
            Participant participant) {
        this.participant = participant;
        this.content = content;
        this.createdAt = createdAt;
        participant.addChat(this);
    }

    /*@PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }*/
}
