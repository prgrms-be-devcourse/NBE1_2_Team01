package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private String content;

    private LocalDateTime createdAt;

}
