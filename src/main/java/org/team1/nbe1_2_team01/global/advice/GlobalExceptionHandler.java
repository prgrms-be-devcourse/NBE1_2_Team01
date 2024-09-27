package org.team1.nbe1_2_team01.global.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team1.nbe1_2_team01.domain.board.exception.BoardNotUpdatedException;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundBoardException;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundTypeException;
import org.team1.nbe1_2_team01.global.util.ExceptionTemplate;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 각종 예외 처리
     * (다른 에러코드를 보낼 필요가 있다면 메서드 추가 생성 가능)
     */
    @ExceptionHandler({
            NotFoundBoardException.class,
            NotFoundTypeException.class,
            BoardNotUpdatedException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ExceptionTemplate> handleBoardExceptions(RuntimeException e) {
        e.printStackTrace();    //개발 완료 후 삭제해야할 코드

        return buildBadRequestResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    /**
     * @Valid 검증 오류 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionTemplate> handleValidationExceptions(MethodArgumentNotValidException e) {
        e.printStackTrace();    //개발 완료 후 삭제해야할 코드

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return buildBadRequestResponse(errors, HttpStatus.BAD_REQUEST.value());
    }

    // BadRequest 응답 생성
    private ResponseEntity<ExceptionTemplate> buildBadRequestResponse(Object message, int value) {
        return ResponseEntity.badRequest()
                .body(new ExceptionTemplate(value, message));
    }
}
