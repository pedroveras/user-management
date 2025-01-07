package br.com.gs3tecnologia.user_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProfileAlreadyExistsException extends BaseException {
    public ProfileAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
