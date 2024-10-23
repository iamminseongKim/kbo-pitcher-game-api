package kms.kbopitcherapi.api.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private HttpStatus httpStatus;

    private ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.code = httpStatus.value();
        this.message = message;
        this.data = data;
        this.httpStatus = httpStatus;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data, String message) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data, HttpStatus.OK.name());
    }
}
