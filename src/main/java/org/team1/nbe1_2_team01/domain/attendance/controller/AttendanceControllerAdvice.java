//package org.team1.nbe1_2_team01.domain.attendance.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.team1.nbe1_2_team01.domain.attendance.exception.AccessDeniedException;
//import org.team1.nbe1_2_team01.domain.attendance.exception.AlreadyExistException;
//import org.team1.nbe1_2_team01.global.util.ExceptionTemplate;
//
//@Slf4j
//@RestControllerAdvice
//public class AttendanceControllerAdvice {
//
//    /**
//     * 특정 리소스에 접근 권한이 없을 때 발생
//     * @valid - 접근 여부 확인
//     */
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ExceptionTemplate> handleAccessDeniedExceptions(AccessDeniedException e) {
//        log.info(e.getMessage());
//
//        ExceptionTemplate exceptionTemplate = new ExceptionTemplate(403, e.getMessage());
//        return ResponseEntity.status(403)
//                .body(exceptionTemplate);
//    }
//
//    /**
//     * 특정 리소스가 이미 존재할 때 발생
//     * @valid - 리소스 존재 여부 확인
//     */
//    @ExceptionHandler(AlreadyExistException.class)
//    public ResponseEntity<ExceptionTemplate> handleAlreadyExistException(AlreadyExistException e) {
//        log.info(e.getMessage());
//
//        ExceptionTemplate exceptionTemplate = new ExceptionTemplate(400, e.getMessage());
//        return ResponseEntity.badRequest()
//                .body(exceptionTemplate);
//    }
//}
