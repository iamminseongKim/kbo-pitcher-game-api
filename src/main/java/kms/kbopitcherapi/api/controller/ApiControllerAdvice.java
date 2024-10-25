package kms.kbopitcherapi.api.controller;

import kms.kbopitcherapi.api.exception.NoSearchAutoCompleteException;
import kms.kbopitcherapi.api.exception.NotFoundAtMakePlayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundAtMakePlayerException.class)
    public ApiResponse<Object> handleNotFoundAtMakePlayerException(NotFoundAtMakePlayerException e) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST,
                null, e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoSearchAutoCompleteException.class)
    public ApiResponse<Object> handleNoSearchAutoCompleteException(NoSearchAutoCompleteException e) {
        return ApiResponse.of(HttpStatus.OK,null, e.getMessage());
    }

}
