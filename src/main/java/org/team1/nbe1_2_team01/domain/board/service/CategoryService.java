package org.team1.nbe1_2_team01.domain.board.service;

import org.team1.nbe1_2_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.Message;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategoryByBelongings(Long teamId);

    Message addCategory(CategoryRequest categoryRequest);

    Message deleteCategory(Long id);
}
