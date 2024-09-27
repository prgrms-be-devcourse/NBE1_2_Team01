package org.team1.nbe1_2_team01.domain.board.constants;

import lombok.Getter;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundTypeException;

@Getter
public enum CommonBoardType {

    NOTICE("notice"),
    STUDY("study");

    private final String type;

    CommonBoardType(String type) {
        this.type = type;
    }

    public static CommonBoardType getType(String type) {
        for (CommonBoardType enumType : values()) {
            if(enumType.getType().equals(type)) {
                return enumType;
            }
        }
        throw new NotFoundTypeException("존재하지 않는 카테고리입니다.");
    }
}
