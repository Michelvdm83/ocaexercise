package com.mvdmstudy.oca;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ExerciseSyntaxException.class)
    public ResponseEntity<ProblemDetail> exerciseSyntaxExceptionHandler(ExerciseSyntaxException exerciseSyntaxException) {
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exerciseSyntaxException.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
