package org.team1.nbe1_2_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.constants.MessageContent;
import org.team1.nbe1_2_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.board.repository.CategoryRepository;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;
import org.team1.nbe1_2_team01.global.util.Message;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BelongingRepository belongingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoryByBelongings(Long teamId) {
        return categoryRepository.getAllCategoryByTeamId(teamId)
                .orElseGet(ArrayList::new);
    }

    @Override
    @Transactional
    public Message addCategory(CategoryRequest categoryRequest) {
        Belonging belonging = belongingRepository.findById(categoryRequest.teamId())
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));

        Category newCategory = categoryRequest.toEntity(belonging);
        categoryRepository.save(newCategory);
        String addMessage = MessageContent.ADD_CATEGORY_COMPLETED.getMessage();
        return new Message(addMessage);
    }

    @Override
    @Transactional
    public Message deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        String deleteMessage = MessageContent.DELETE_CATEGORY_COMPLETED.getMessage();
        return new Message(deleteMessage);
    }
}
