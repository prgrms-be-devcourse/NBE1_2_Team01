package org.team1.nbe1_2_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Category;

@Getter @ToString
public class CategoryResponse {

    private final Long id;
    private final String name;

    @Builder
    private CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
