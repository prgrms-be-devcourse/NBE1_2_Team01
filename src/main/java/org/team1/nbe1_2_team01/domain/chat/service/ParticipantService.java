package org.team1.nbe1_2_team01.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.chat.controller.request.InviteRequest;
import org.team1.nbe1_2_team01.domain.chat.entity.Channel;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.entity.ParticipantPK;
import org.team1.nbe1_2_team01.domain.chat.repository.ChannelRepository;
import org.team1.nbe1_2_team01.domain.chat.repository.ParticipantRepository;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.team1.nbe1_2_team01.global.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final UserChannelUtil userChannelUtil;

    // 채널에 참여
    @Transactional
    public Participant joinChannel(Long channelId, Long userId) {

        // 이미 존재하는 참여자인지 확인
        ParticipantPK participantPK = new ParticipantPK(userId, channelId);
        return participantRepository.findById(participantPK).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new AppException(INVITER_NOT_FOUND));
            Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new AppException(CHANEL_NOT_FOUND));


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
    public void inviteUser(InviteRequest inviteRequest) {
        ParticipantPK participantPK = new ParticipantPK(inviteRequest.getInviteUserId(), inviteRequest.getChannelId());
        Participant inviter = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(INVITER_NOT_FOUND));

        if (!inviter.isCreator()) {
            throw new AppException(NOT_CHANEL_CREATOR);
        }

        joinChannel(inviteRequest.getChannelId(), inviteRequest.getParticipantId());
    }

    // 참여중인 채널 조회
    @Transactional(readOnly = true)
    public List<Channel> checkUserChannel(Long userId) {
        List<Participant> participants = participantRepository.findByUserId(userId);

        return participants.stream()
                .map(Participant::getChannel) // 참여중인 채널 리스트로 반환
                .collect(Collectors.toList());
    }

    // 참여자가 스스로 방을 나감
    @Transactional
    public void leaveChannel(ParticipantPK participantPK) {
        Participant participant = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(PARTICIPANTS_NOT_FOUND));;
        participantRepository.delete(participant);
    }

    // 방장이 참여자를 강퇴
    @Transactional
    public void removeParticipant(ParticipantPK participantPK, Long participantIdToRemove) {
        // 현재 참여자 정보 가져옴
        Participant participant = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(NO_PARTICIPANTS));

        // 채널 생성자가 아님
        if (!participant.isCreator()) {
            throw new AppException(NOT_CHANEL_CREATOR);
        }

        // 강퇴자 찾기
        Participant participantToRemove = participantRepository.findById(new ParticipantPK(participantIdToRemove, participantPK.getChannelId()))
                .orElseThrow(() -> new AppException(PARTICIPANTS_NOT_FOUND));

        participantRepository.delete(participantToRemove);
    }

}