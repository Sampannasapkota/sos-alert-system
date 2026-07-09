package com.hgn.sosalert.common.exception;

import com.hgn.sosalert.feature.device.exception.DeviceAlreadyExistsException;
import com.hgn.sosalert.shared.enums.ResponseStatus;
import com.hgn.sosalert.shared.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private ResponseEntity<ApiResponseDto<?>> buildErrorResponse(String message, HttpStatus status) {
        ApiResponseDto<?> response = new ApiResponseDto<>(ResponseStatus.ERROR.value, message, status);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<?>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeviceAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> handleDeviceAlreadyExistsException(DeviceAlreadyExistsException ex) {
        log.error("Device already exists.", ex);
        return buildErrorResponse("Device already exists.", HttpStatus.BAD_REQUEST);
    }

}
