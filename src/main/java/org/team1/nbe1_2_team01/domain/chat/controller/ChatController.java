package org.team1.nbe1_2_team01.domain.chat.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.chat.controller.dto.ChatMessageDTO;
import org.team1.nbe1_2_team01.domain.chat.entity.Chat;
import org.team1.nbe1_2_team01.domain.chat.service.ChatService;
import org.team1.nbe1_2_team01.domain.chat.service.ParticipantService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/chat/{channelId}")
    @SendTo("/topic/chat/{channelId}")
    public ChatMessageDTO sendMessage(@DestinationVariable Long channelId, @Payload ChatMessageDTO messageDTO) {

        System.out.println("Received messageDTO: " + messageDTO);

        try {
            Chat chat = chatService.createChat(messageDTO.getChannelId(), messageDTO.getContent(), messageDTO.getUserId());

            return ChatMessageDTO.builder()
                    .channelId(channelId) // channelId를 사용
                    .userId(chat.getParticipant().getUserId()) // 참가자의 userId를 가져옴
                    .content(chat.getContent()) // 저장된 메시지를 가져옴
                    .createdAt(LocalDateTime.now()) // 현재 시간으로 설정
                    .build();

        } catch (EntityNotFoundException e) {
            System.err.println("EntityNotFoundException: " + e.getMessage());
            System.out.println("여기 걸린거여 ");
        }
        return messageDTO;
    }
}