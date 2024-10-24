package kms.kbopitcherapi.api.controller;

import kms.kbopitcherapi.api.controller.csv.exception.NotFoundAtMakePlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundAtMakePlayerException.class)
    public ApiResponse<Object> handleNotFoundAtMakePlayerException(NotFoundAtMakePlayerException ex) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST,
                null, ex.getMessage());
    }

}
