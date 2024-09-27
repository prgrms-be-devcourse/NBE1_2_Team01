package org.team1.nbe1_2_team01.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.chat.controller.dto.ChannelRequest;
import org.team1.nbe1_2_team01.domain.chat.entity.Channel;
import org.team1.nbe1_2_team01.domain.chat.service.ChannelService;

@RequestMapping("/channel")
@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/createChannel")
    public ResponseEntity<Channel> createChannel(@RequestBody ChannelRequest channelRequest) {
        Channel channel = channelService.createChannel(channelRequest.getChannelName(), channelRequest.getCreatorUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }
}
