package org.team1.nbe1_2_team01.domain.board.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team1.nbe1_2_team01.domain.board.exception.BoardNotUpdatedException;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundBoardException;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundTypeException;
import org.team1.nbe1_2_team01.global.util.ExceptionTemplate;

@RestControllerAdvice
public class BoardExceptionHandler {

    /**
     * 각종 예외 처리
     * (다른 에러코드를 보낼 필요가 있다면 메서드 추가 생성 가능)
     */
    @ExceptionHandler({
            BoardNotUpdatedException.class,
            NotFoundBoardException.class,
            NotFoundTypeException.class
    })
    public ResponseEntity<ExceptionTemplate> handleBoardExceptions(RuntimeException e) {
        e.printStackTrace();    //개발 완료 후 삭제해야할 코드
        return buildBadRequestResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    // BadRequest 응답 생성
    private ResponseEntity<ExceptionTemplate> buildBadRequestResponse(Object message, int value) {
        return ResponseEntity.badRequest()
                .body(new ExceptionTemplate(value, message));
    }
}
