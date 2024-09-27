package org.team1.nbe1_2_team01.domain.chat.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.chat.controller.dto.ChannelDTO;
import org.team1.nbe1_2_team01.domain.chat.controller.dto.ChannelJoinDTO;
import org.team1.nbe1_2_team01.domain.chat.controller.dto.InviteDTO;
import org.team1.nbe1_2_team01.domain.chat.entity.Channel;
import org.team1.nbe1_2_team01.domain.chat.entity.Participant;
import org.team1.nbe1_2_team01.domain.chat.service.ParticipantService;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/participants")
@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/joinChannel")
    public ResponseEntity<Participant> joinChannel(@RequestBody ChannelJoinDTO channelJoinDTO) {

        Participant participant = participantService.joinChannel(channelJoinDTO.getChannelId(), channelJoinDTO.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(participant);

    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteUser(@RequestBody InviteDTO inviteDTO) {
        try {
            participantService.inviteUser(inviteDTO);
            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 초대하였습니다.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("채널 생성자만이 초대할 수 있습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/channels")
    public ResponseEntity<List<ChannelDTO>> checkUserChannels(@PathVariable("userId") Long userId) {
        List<Channel> channels = participantService.checkUserChannel(userId);

        List<ChannelDTO> channelDTOS = channels.stream()
                .map(channel -> new ChannelDTO(channel.getId(), channel.getChannelName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(channelDTOS);
    }
}