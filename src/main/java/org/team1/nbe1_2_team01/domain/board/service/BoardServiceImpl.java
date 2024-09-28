package org.team1.nbe1_2_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.comment.service.CommentService;
import org.team1.nbe1_2_team01.domain.board.comment.service.response.CommentResponse;
import org.team1.nbe1_2_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_2_team01.domain.board.constants.MessageContent;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardDeleteRequest;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardRequest;
import org.team1.nbe1_2_team01.domain.board.controller.dto.BoardUpdateRequest;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundBoardException;
import org.team1.nbe1_2_team01.domain.board.repository.BoardRepository;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.Message;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

import static org.team1.nbe1_2_team01.domain.board.constants.MessageContent.NOT_EXIST_BOARD;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CommentService commentService;
    private final BelongingRepository belongingRepository;
    private final UserRepository userRepository;

    private static final int PAGE_SIZE = 10;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> getCommonBoardList(Long courseLid, String type, int page) {
        //pageable 생성
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        //쿼리로 데이터 가져오기.
        CommonBoardType boardType = CommonBoardType.getType(type);
        return boardRepository.findAllCommonBoard(boardType, courseLid, pageable)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Message addCommonBoard(BoardRequest boardRequest) {
        User user = getUser();
        if(boardRequest.isNotice() && user.getRole().equals(Role.USER)) {
            throw new IllegalArgumentException("관리자만 이용가능합니다.");
        }

        Long courseId = boardRequest.getCourseId();
        Belonging course = belongingRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("소속이 존재하지 않습니다"));

        Board newBoard = boardRequest.toEntity(user, null, course);
        boardRepository.save(newBoard);

        return new Message(MessageContent.getAddMessage(boardRequest.isNotice()));
    }

    private User getUser() {
        String currentUsername = SecurityUtil.getCurrentUsername(); //id를 반환
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetailById(Long id) {
        //게시글 정보를 가져와.
        BoardDetailResponse boardDetailResponse = boardRepository.findBoardDetailExcludeComments(id)
                .orElseThrow(() -> new NotFoundBoardException(NOT_EXIST_BOARD.getMessage()));

        //게시글에 해당하는 리뷰를 가져와
        List<CommentResponse> comments = commentService.getReviewsByPage(id, 0);
        boardDetailResponse.addComments(comments);

        return boardDetailResponse;
    }

    @Override
    public Message deleteBoardById(BoardDeleteRequest request) {
        //내가 작성한 게시글인지 확인?

        boardRepository.deleteById(request.boardId());
        String deleteMessage = MessageContent.getDeleteMessage(request.isNotice());
        return new Message(deleteMessage);
    }

    @Override
    public Message updateBoard(BoardUpdateRequest updateRequest) {
        Board findBoard = boardRepository.findById(updateRequest.boardId())
                .orElseThrow(() -> new NotFoundBoardException(NOT_EXIST_BOARD.getMessage()));

        findBoard.updateBoard(updateRequest);
        String updateMessage = MessageContent.getUpdateMessage(updateRequest.isNotice());
        return new Message(updateMessage);
    }

    @Override
    public List<BoardResponse> getTeamBoardListByType(Long belongingId, Long categoryId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return boardRepository.findAllTeamBoardDByType(belongingId, categoryId, pageable)
                .orElseGet(ArrayList::new);
    }
}
