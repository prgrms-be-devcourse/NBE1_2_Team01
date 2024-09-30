package org.team1.nbe1_2_team01.domain.chat.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatDTO {
    private Long UserId;
    private String content;
    private String userName; // 이름
    private LocalDateTime createdAt;
}
