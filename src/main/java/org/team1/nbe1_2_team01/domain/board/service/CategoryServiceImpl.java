package org.team1.nbe1_2_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_2_team01.domain.board.repository.CategoryRepository;
import org.team1.nbe1_2_team01.domain.board.service.extractor.UserExtractor;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoryByBelongings(Long teamId) {
        //사용자의 소속 데이터를 가져와(팀장의 소속 번호를 가져와야할거 같은데?)
        UserExtractor.extract();

        return categoryRepository.findAllByBelonging_Id(null).stream()
                .map(CategoryResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public String addCategory(CategoryRequest categoryRequest) {
        //사용자의 소속을 가져와.

        //게시글 저장
        categoryRepository.save(categoryRequest.toEntity(null));
        return "카테고리를 등록했습니다.";
    }
}
