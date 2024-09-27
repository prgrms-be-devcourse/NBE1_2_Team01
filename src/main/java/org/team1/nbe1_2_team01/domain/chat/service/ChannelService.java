package org.team1.nbe1_2_team01.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.chat.entity.Channel;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.repository.ChannelRepository;
import org.team1.nbe1_2_team01.domain.chat.repository.ParticipantRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Transactional
    public Channel createChannel(String channelName, Long creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Channel channel = Channel.builder()
                .channelName(channelName)
                .build();

        Channel saveChannel = channelRepository.save(channel);

        // 참여자 추가
        Participant participant = Participant.builder()
                .user(creator)
                .channel(channel)
                .isCreator(true) // 생성자니까 true
                .participatedAt(LocalDateTime.now())
                .isParticipated(true) // 필요에 따라 설정
                .build();

        participantRepository.save(participant);

        return saveChannel;
    }


}
