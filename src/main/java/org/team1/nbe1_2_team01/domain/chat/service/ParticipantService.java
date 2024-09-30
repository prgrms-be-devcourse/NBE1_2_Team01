package org.team1.nbe1_2_team01.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.chat.controller.request.InviteDTO;
import org.team1.nbe1_2_team01.domain.chat.entity.Channel;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.entity.ParticipantPK;
import org.team1.nbe1_2_team01.domain.chat.repository.ChannelRepository;
import org.team1.nbe1_2_team01.domain.chat.repository.ParticipantRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    // 채널에 참여
    public Participant joinChannel(Long channelId, Long userId) {

        // 이미 존재하는 참여자인지 확인
        ParticipantPK participantPK = new ParticipantPK(userId, channelId);
        return participantRepository.findById(participantPK).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
            Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new EntityNotFoundException("채널을 찾을 수 없습니다"));


            Participant participant = Participant.builder()
                    .isCreator(false)  // 초대받으면 생성자 x
                    .participatedAt(LocalDateTime.now())  // 참여 시간 설정
                    .isParticipated(true)  // 참여 여부 설정
                    .user(user)
                    .channel(channel)
                    .build();

            return participantRepository.save(participant);
        });
    }


    // 사용자 초대
    @Transactional
    public void inviteUser(InviteDTO inviteDTO) {
        ParticipantPK participantPK = new ParticipantPK(inviteDTO.getInviteUserId(), inviteDTO.getChannelId());
        Participant inviter = participantRepository.findById(participantPK)
                .orElseThrow(() -> new EntityNotFoundException("초대자를 찾을 수 없음."));

        if (!inviter.isCreator()) {
            throw new SecurityException("채널 생성자만이 초대를 할 수 있습니다.");
        }

        joinChannel(inviteDTO.getChannelId(), inviteDTO.getParticipantId());
    }

    // 참여중인 채널 조회
    public List<Channel> checkUserChannel(Long userId) {
        List<Participant> participants = participantRepository.findByUserId(userId);

        return participants.stream()
                .map(Participant :: getChannel) // 참여중인 채널 리스트로 반환
                .collect(Collectors.toList());
    }

}
