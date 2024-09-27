package org.team1.nbe1_2_team01.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_2_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_2_team01.domain.board.service.CategoryService;
import org.team1.nbe1_2_team01.domain.board.service.response.CategoryResponse;
import org.team1.nbe1_2_team01.domain.board.service.response.Message;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 소속에 맞는 카테고리 가져오기
     * (이 부분은 나중에 한번 확인하고 수정 필요)
     * @return
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam("team") Long teamId) {
        return ResponseEntity.ok().body(categoryService.getAllCategoryByBelongings(teamId));
    }

    /**
     * 새로운 카테고리 생성
     * @param categoryRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok().body(categoryService.addCategory(categoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok().body(categoryService.deleteCategory(id));

    }
}
