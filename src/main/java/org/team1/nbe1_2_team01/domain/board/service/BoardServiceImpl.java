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
import org.team1.nbe1_2_team01.domain.board.repository.BoardRepository;
import org.team1.nbe1_2_team01.domain.board.repository.CategoryRepository;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardResponse;
import org.team1.nbe1_2_team01.domain.board.service.valid.BoardValidator;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.global.util.Message;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BelongingRepository belongingRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> getCommonBoardList(BoardListRequest boardListRequest) {
        CommonBoardType boardType = CommonBoardType.getType(boardListRequest.type());

        return boardRepository.findAllCommonBoard(
                boardType,
                boardListRequest.courseId(),
                boardListRequest.boardId()
        );
    }

    @Override
    public Message addBoard(BoardRequest boardRequest) {
        User user = getUser();
        BoardValidator.validateAdminForNotice(user, boardRequest.isNotice());

        Long belongingId = boardRequest.belongingId();
        Belonging course = belongingRepository.findById(belongingId)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));

        log.info("course = {}", course);

        Long categoryId = boardRequest.categoryId();
        Category category = null;
        if(categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        Board newBoard = boardRequest.toEntity(user, category, course);
        boardRepository.save(newBoard);

        return new Message(MessageContent.getAddMessage(boardRequest.isNotice()));
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetailById(Long id) {
        return boardRepository.findBoardDetailExcludeComments(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Override
    public Message deleteBoardById(BoardDeleteRequest request) {
        Board findBoard = getBoardWithValidateUser(
                request.boardId(),
                request.isNotice()
        );
        boardRepository.delete(findBoard);

        String deleteMessage = MessageContent.getDeleteMessage(request.isNotice());
        return new Message(deleteMessage);
    }


    @Override
    public Message updateBoard(BoardUpdateRequest updateRequest) {
        Board findBoard = getBoardWithValidateUser(
                updateRequest.boardId(),
                updateRequest.isNotice()
        );
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
                boardId
        );
    }
    private Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
    }

    private User getUser() {
        String currentUsername = SecurityUtil.getCurrentUsername(); //id를 반환
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private Board getBoardWithValidateUser(Long boardId, boolean isNotice) {
        Board findBoard = getBoardById(boardId);
        User user = getUser();

        BoardValidator.validateWriter(findBoard, user);
        BoardValidator.validateAdminForNotice(user, isNotice);
        return findBoard;
    }
}
