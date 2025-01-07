package br.com.gs3tecnologia.user_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BaseException extends RuntimeException {
    private final ProblemDetail problemDetail;

    public BaseException(HttpStatus status, String message) {
        this.problemDetail = ProblemDetail.forStatusAndDetail(status, message);
    }

    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }
}
