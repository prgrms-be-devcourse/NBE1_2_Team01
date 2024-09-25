package org.team1.nbe1_2_team01.domain.board.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_2_team01.domain.board.comment.service.CommentService;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@RequestParam(name = "p") int page,
                                                             @RequestParam("boardId") Long boardId) {
        return ResponseEntity.ok().body(commentService.getReviewsByPage(boardId, page));
    }
}
