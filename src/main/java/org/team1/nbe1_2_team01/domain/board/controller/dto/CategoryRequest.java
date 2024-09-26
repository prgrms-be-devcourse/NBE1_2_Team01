package org.team1.nbe1_2_team01.domain.board.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;


@Getter @ToString
public class CategoryRequest {

    private final String name;

    @Builder
    private CategoryRequest(String name) {
        this.name = name;
    }

    public Category toEntity(Belonging belonging) {
        return Category.builder()
                .belonging(belonging)
                .name(name)
                .build();
    }
}
