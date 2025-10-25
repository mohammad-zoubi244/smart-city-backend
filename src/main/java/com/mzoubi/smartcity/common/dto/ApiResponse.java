package com.mzoubi.smartcity.common.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiResponse <T>(
        boolean success,
        String message,
        T data,
        int status,
        LocalDateTime timestamp
){
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return new ApiResponse<>(true, message, data, status.value(), LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return new ApiResponse<>(false, message, null, status.value(), LocalDateTime.now());
    }
}
