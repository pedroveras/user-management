package br.com.gs3tecnologia.user_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProfileNotFoundException extends BaseException {
    public ProfileNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
