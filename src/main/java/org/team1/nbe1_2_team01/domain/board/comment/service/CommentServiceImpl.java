package org.team1.nbe1_2_team01.domain.board.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.comment.repository.CommentRepository;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getReviewsByPage(Long boardId, int page) {
        return commentRepository.getCommentsByBoardId(boardId, getPageable(page))
                .orElseGet(ArrayList::new);
    }

    private Pageable getPageable(int page) {
        int PAGE_SIZE = 10;
        return PageRequest.of(page, PAGE_SIZE);
    }
}
