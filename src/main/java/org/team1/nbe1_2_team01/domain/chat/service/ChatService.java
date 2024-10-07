package org.team1.nbe1_2_team01.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.chat.entity.Chat;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.entity.ParticipantPK;
import org.team1.nbe1_2_team01.domain.chat.repository.ChatRepository;
import org.team1.nbe1_2_team01.domain.chat.repository.ParticipantRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

import java.time.LocalDateTime;
import java.util.List;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Chat createChat(Long channelId, String message, Long userId) {
        ParticipantPK participantPK = new ParticipantPK(userId, channelId);

        Participant participant = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(PARTICIPANTS_NOT_FOUND));

        // 채널 ID를 통해 참가자가 해당 채널에 소속되어 있는지 확인
        if (!participant.getChannelId().equals(channelId)) {
            throw new AppException(NO_PARTICIPANTS);
        }

        return chatRepository.save(Chat.builder()
                .content(message)
                .createdAt(LocalDateTime.now())
                .participant(participant)
                .build());
    }

    // 채팅 불러오기
    @Transactional(readOnly = true)
    public List<Chat> getChatsByChannelId(Long channelId) {
        List<Chat> chats = chatRepository.findByParticipant_Channel_Id(channelId);

        if (chats.isEmpty()) {
            throw new AppException(NOT_CHAT);
        }
        return chatRepository.findByParticipant_Channel_Id(channelId);
    }
}
