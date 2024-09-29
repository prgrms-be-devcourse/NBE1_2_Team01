package org.team1.nbe1_2_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_2_team01.domain.board.constants.MessageContent;
import org.team1.nbe1_2_team01.domain.board.controller.dto.*;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundBoardException;
import org.team1.nbe1_2_team01.domain.board.repository.BoardRepository;
import org.team1.nbe1_2_team01.domain.board.repository.CategoryRepository;
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
    private final BelongingRepository belongingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> getCommonBoardList(BoardListRequest boardListRequest) {

        //쿼리로 데이터 가져오기.
        CommonBoardType boardType = CommonBoardType.getType(boardListRequest.type());

        return boardRepository.findAllCommonBoard(
                        boardType,
                        boardListRequest.courseId(),
                        boardListRequest.boardId()
                )
                .orElseGet(ArrayList::new);
    }

    @Override
    public Message addBoard(BoardRequest boardRequest) {
        User user = getUser();
        if(boardRequest.isNotice() && user.getRole().equals(Role.USER)) {
            throw new IllegalArgumentException("관리자만 이용가능합니다.");
        }

        Long belongingId = boardRequest.belongingId();
        Belonging course = belongingRepository.findById(belongingId)
                .orElseThrow(() -> new IllegalArgumentException("소속이 존재하지 않습니다"));

        //CategoryService에 있으면 좋을 코드일거 같은데 임시로 넣어놨습니다.
        Category category = null;
        if(boardRequest.categoryId() != null) {
            category = categoryRepository.findById(boardRequest.categoryId())
                    .orElse(null);
        }

        Board newBoard = boardRequest.toEntity(user, category, course);
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
        return boardRepository.findBoardDetailExcludeComments(id)
                .orElseThrow(() -> new NotFoundBoardException(NOT_EXIST_BOARD.getMessage()));
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
    public List<BoardResponse> getTeamBoardListByType(TeamBoardListRequest request) {
        Long belongingId = request.belongingId();
        Long categoryId = request.categoryId();
        Long boardId = request.boardId();

        return boardRepository.findAllTeamBoardDByType(
                        belongingId,
                        categoryId,
                        boardId)
                .orElseGet(ArrayList::new);
    }
}
