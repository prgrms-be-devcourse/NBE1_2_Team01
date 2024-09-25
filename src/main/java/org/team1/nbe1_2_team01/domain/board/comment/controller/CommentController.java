package org.team1.nbe1_2_team01.domain.board.comment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        return ResponseEntity.ok().body(commentService.deleteById(id));
    }
}
