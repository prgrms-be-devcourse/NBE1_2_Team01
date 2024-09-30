package org.team1.nbe1_2_team01.domain.chat.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.chat.service.response.ChatMessageResponse;
import org.team1.nbe1_2_team01.domain.chat.service.response.ChatResponse;
import org.team1.nbe1_2_team01.domain.chat.controller.request.ChatMessageRequest;
import org.team1.nbe1_2_team01.domain.chat.entity.Chat;
import org.team1.nbe1_2_team01.domain.chat.service.ChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    // 메시지를 보내는 컨트롤러
    @MessageMapping("/chat/{channelId}")
    @SendTo("/topic/chat/{channelId}")
    public ChatMessageResponse sendMessage(@DestinationVariable Long channelId, @Payload ChatMessageRequest msgRequest) {
        try {
            Chat chat = chatService.createChat(msgRequest.getChannelId(), msgRequest.getContent(), msgRequest.getUserId());

            return ChatMessageResponse.builder()
                    .channelId(channelId) // channelId를 사용
                    .userId(chat.getParticipant().getUserId()) // 참가자의 userId를 가져옴
                    .content(chat.getContent()) // 저장된 메시지를 가져옴
                    .createdAt(LocalDateTime.now()) // 현재 시간으로 설정
                    .build();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null; // 추후에 예외처리
    }

    // 채팅방 목록을 불러오기
    @GetMapping("/api/chats/{channelId}")
    public ResponseEntity<List<ChatResponse>> getChatsByChannelId(@PathVariable("channelId") Long channelId) {
        System.out.println("Received channelId: " + channelId); // 로그 추가

        List<Chat> chats = chatService.getChatsByChannelId(channelId);
        List<ChatResponse> chatResponses = chats.stream()
                .map(chat -> new ChatResponse(chat.getId(), chat.getContent(), chat.getParticipant().getUser().getName(), chat.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(chatResponses);
    }
}