package org.team1.nbe1_2_team01.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.domain.board.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByBelonging_Id(Long belongingId);
}
