package org.team1.nbe1_2_team01.domain.board.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.service.BoardService;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/team")
public class TeamBoardController {

    private final BoardService boardService;

    @GetMapping("/{belongingId}")
    public ResponseEntity<List<BoardResponse>> getTeamBoardList(
        @PathVariable Long belongingId,
        @RequestParam Long categoryId,
        @RequestParam int page
    ) {
        return ResponseEntity.ok().body(boardService.getTeamBoardListByType(belongingId, categoryId, page));
    }
}
