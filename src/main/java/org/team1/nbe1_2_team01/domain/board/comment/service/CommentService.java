package org.team1.nbe1_2_team01.domain.board.comment.service;

import org.team1.nbe1_2_team01.domain.board.comment.controller.dto.CommentRequest;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.Message;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getReviewsByPage(Long boardId, int page);

    Message deleteById(Long id);

    Message addComment(CommentRequest commentRequest);
}
