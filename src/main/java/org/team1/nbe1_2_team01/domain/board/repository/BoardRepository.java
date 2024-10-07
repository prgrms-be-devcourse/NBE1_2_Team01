package org.team1.nbe1_2_team01.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.domain.board.entity.TeamBoard;


@Repository
public interface BoardRepository extends JpaRepository<TeamBoard, Long>, CustomBoardRepository {

}
