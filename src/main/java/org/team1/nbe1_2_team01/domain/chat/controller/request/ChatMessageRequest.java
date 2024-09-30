package org.team1.nbe1_2_team01.domain.chat.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long channelId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "channelId=" + channelId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

