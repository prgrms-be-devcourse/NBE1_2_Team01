package org.team1.nbe1_2_team01.global.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundBoardException;
import org.team1.nbe1_2_team01.domain.board.exception.NotFoundTypeException;
import org.team1.nbe1_2_team01.global.util.ErrorResult;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {NotFoundBoardException.class, NotFoundTypeException.class})
    public ResponseEntity<ErrorResult> BoardExceptionHandler(RuntimeException e) {
        e.printStackTrace();

        return ResponseEntity.badRequest()
                .body(new ErrorResult(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage()
                ));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResult> illegalArgumentExceptionHandler(RuntimeException e) {
        e.printStackTrace();

        return ResponseEntity.badRequest()
                .body(new ErrorResult(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage()
                ));
    }

    /**
     * @Valid 검증 오류 처리
     * @param
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(new ErrorResult(
                        HttpStatus.BAD_REQUEST.value(),
                        errors
                ));
    }
}
