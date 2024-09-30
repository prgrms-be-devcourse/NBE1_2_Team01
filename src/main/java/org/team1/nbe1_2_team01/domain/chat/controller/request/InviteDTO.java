package org.team1.nbe1_2_team01.domain.chat.controller.request;

import lombok.Getter;

@Getter
public class InviteDTO {
    private Long inviteUserId; // 초대자
    private Long participantId; // 참여자
    private Long channelId;

    public InviteDTO(Long inviteUserId, Long participantId, Long channelId) {
        this.inviteUserId = inviteUserId;
        this.participantId = participantId;
        this.channelId = channelId;
    }

    public InviteDTO() {
    }
}
