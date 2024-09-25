package org.team1.nbe1_2_team01.domain.chat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @NotNull
    private String channelName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "channel")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    private Channel(String channelName) {
        this.channelName = channelName;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }
}
