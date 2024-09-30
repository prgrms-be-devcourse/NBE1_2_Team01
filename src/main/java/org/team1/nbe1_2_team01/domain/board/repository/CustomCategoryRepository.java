package org.team1.nbe1_2_team01.domain.board.repository;

import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CustomCategoryRepository {


    Optional<List<CategoryResponse>> getAllCategoryByTeamId(Long teamId);
}
