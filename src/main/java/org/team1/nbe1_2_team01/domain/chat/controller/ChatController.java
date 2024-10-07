package org.team1.nbe1_2_team01.domain.chat.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.function.array.ArrayAndElementArgumentTypeResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    // 메시지를 보내는 컨트롤러
    @MessageMapping("/chat/{channelId}")
    @SendTo("/topic/chat/{channelId}")
    public ChatMessageResponse sendMessage(@DestinationVariable Long channelId, @Payload ChatMessageRequest msgRequest) {
        return chatService.sendMessage(channelId, msgRequest);
    }

    // 채팅방 목록을 불러오기
    // (지속적으로 업데이트 되는 방식? 계속해서 호출되는 방식? 이라 일단 트랜잭션 안 붙였습니다.)
    @GetMapping("/chats/{channelId}")
    public ResponseEntity<List<ChatResponse>> getChatsByChannelId(@PathVariable("channelId") Long channelId) {
        List<ChatResponse> chatResponses = chatService.getChatsByChannelId(channelId);
        return ResponseEntity.ok(chatResponses);
    }
}