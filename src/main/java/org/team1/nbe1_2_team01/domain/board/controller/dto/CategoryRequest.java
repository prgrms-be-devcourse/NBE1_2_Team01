package org.team1.nbe1_2_team01.domain.board.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.team1.nbe1_2_team01.domain.board.entity.Category;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;



public record CategoryRequest(
        @NotNull(message = "카테고리 등록에 실패했습니다.")
        @NotBlank(message = "카테고리 등록에 실패했습니다.")
        Long teamId,

        @NotNull(message = "카테고리명은 필수값입니다.")
        String name
) {
    public Category toEntity(Belonging belonging) {
        return Category.builder()
                .belonging(belonging)
                .name(name)
                .build();
    }
}
