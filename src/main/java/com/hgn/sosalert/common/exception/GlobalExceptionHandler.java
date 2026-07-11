package com.hgn.sosalert.common.exception;

import com.hgn.sosalert.feature.device.exception.DeviceAlreadyExistsException;
import com.hgn.sosalert.feature.trekGroup.exception.TrekGroupAlreadyExistsException;
import com.hgn.sosalert.feature.trekGroup.exception.TrekGroupNotFoundException;
import com.hgn.sosalert.feature.trekker.exception.TrekkerNotFoundException;
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

    @ExceptionHandler(TrekGroupAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> handleTrekGroupAlreadyExistsException(TrekGroupAlreadyExistsException ex) {
        log.error("Trek group already exists.", ex);
        return buildErrorResponse("Trek group already exists.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TrekGroupNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleTrekGroupNotFoundException(TrekGroupNotFoundException ex) {
        log.error("Trek group not found.", ex);
        return buildErrorResponse("Trek group not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TrekkerNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleTrekkerNotFoundException(TrekkerNotFoundException ex) {
        log.error("Trekker not found.", ex);
        return buildErrorResponse("Trekker not found", HttpStatus.BAD_REQUEST);
    }

}
