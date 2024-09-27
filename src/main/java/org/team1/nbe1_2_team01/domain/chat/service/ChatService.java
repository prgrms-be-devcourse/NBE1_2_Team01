package org.team1.nbe1_2_team01.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.chat.entity.Chat;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.entity.ParticipantPK;
import org.team1.nbe1_2_team01.domain.chat.repository.ChatRepository;
import org.team1.nbe1_2_team01.domain.chat.repository.ParticipantRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ParticipantRepository participantRepository;


    public Chat createChat(Long channelId, String message, Long userId) {
        ParticipantPK participantPK = new ParticipantPK(userId, channelId);
        System.out.println("이건 안나오나" + participantPK);

        Participant participant = participantRepository.findById(participantPK)
                .orElseThrow(() -> new EntityNotFoundException("참여자 userId: " + userId + ", channelId: " + channelId + " 가 존재하지 않습니다."));


        // 채널 ID를 통해 참가자가 해당 채널에 소속되어 있는지 확인
        if (!participant.getChannelId().equals(channelId)) {
            throw new IllegalArgumentException("참여자가 없음 ");
        }

        return chatRepository.save(Chat.builder()
                .content(message)
                .createdAt(LocalDateTime.now())
                .participant(participant)
                .build());
    }
}
