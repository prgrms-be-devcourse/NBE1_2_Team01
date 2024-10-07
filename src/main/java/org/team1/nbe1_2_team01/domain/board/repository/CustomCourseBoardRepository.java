package org.team1.nbe1_2_team01.domain.board.repository;

import org.team1.nbe1_2_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_2_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.CourseBoardResponse;

import java.util.List;
import java.util.Optional;

public interface CustomCourseBoardRepository {

    List<CourseBoardResponse> findAllCourseBoard(CommonBoardType type, Long belongId, Long boardId);

    Optional<BoardDetailResponse> findCourseBoardDetailById(Long id);

}
