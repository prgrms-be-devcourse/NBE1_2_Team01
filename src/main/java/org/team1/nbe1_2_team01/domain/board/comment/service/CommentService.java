package org.team1.nbe1_2_team01.domain.board.comment.service;

import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getReviewsByPage(Long boardId, int page);
}
