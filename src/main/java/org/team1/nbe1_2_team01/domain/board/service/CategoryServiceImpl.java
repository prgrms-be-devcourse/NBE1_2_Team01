package org.team1.nbe1_2_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.constants.MessageContent;
import org.team1.nbe1_2_team01.domain.board.controller.dto.CategoryDeleteRequest;
import org.team1.nbe1_2_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.board.repository.CategoryRepository;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;
import org.team1.nbe1_2_team01.domain.board.service.valid.CategoryValidator;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_2_team01.global.exception.AppException;
import org.team1.nbe1_2_team01.global.util.ErrorCode;
import org.team1.nbe1_2_team01.global.util.Message;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.global.util.SecurityUtil;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BelongingRepository belongingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoryByBelongings(Long teamId) {
        return categoryRepository.getAllCategoryByTeamId(teamId);
    }

    @Override
    public Message addCategory(CategoryRequest categoryRequest) {
        Belonging belonging = getBelongingWithValidation(categoryRequest.teamId());

        Category newCategory = categoryRequest.toEntity(belonging);
        categoryRepository.save(newCategory);

        String addMessage = MessageContent.ADD_CATEGORY_COMPLETED.getMessage();
        return new Message(addMessage);
    }

    @Override
    public Message deleteCategory(CategoryDeleteRequest request) {
        //요청한 사용자가 팀장인지?
        Long teamId = request.teamId();
        Long categoryId = request.categoryId();

        Belonging belonging = getBelongingWithValidation(teamId);
        int result = categoryRepository.deleteByIdAndBelonging_Id(categoryId, belonging.getId());

        if(result == 0) {
            throw new AppException(ErrorCode.CATEGORY_NOT_DELETED);
        }

        String deleteMessage = MessageContent.DELETE_CATEGORY_COMPLETED.getMessage();
        return new Message(deleteMessage);
    }

    /**
     * 요청한 사용자가 팀장인지 검증하고
     * 소속 엔티티를 반환하는 코드
     * @param teamId
     * @return
     */
    private Belonging getBelongingWithValidation(Long teamId) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        Belonging belonging = belongingRepository.findByTeam_IdAndUser_Username(
                teamId,
                currentUsername
        ).orElseThrow(() -> new AppException(ErrorCode.BELONGING_NOT_FOUND));

        CategoryValidator.validateTeamLeader(belonging);

        return belonging;
    }
}
