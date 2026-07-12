package com.hgn.sosalert.common.exception;

import com.hgn.sosalert.feature.alert.exception.AlertAssignmentAmbiguousException;
import com.hgn.sosalert.feature.alert.exception.AlertAssignmentNotFoundException;
import com.hgn.sosalert.feature.alert.exception.AlertNotFoundException;
import com.hgn.sosalert.feature.device.exception.DeviceAlreadyExistsException;
import com.hgn.sosalert.feature.device.exception.DeviceNotFoundException;
import com.hgn.sosalert.feature.deviceAssignment.exception.AssignmentConflictException;
import com.hgn.sosalert.feature.deviceAssignment.exception.DeviceAssignmentNotFoundException;
import com.hgn.sosalert.feature.order.exception.OrderAlreadyExistsException;
import com.hgn.sosalert.feature.order.exception.OrderNotFoundException;
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

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleDeviceNotFoundException(DeviceNotFoundException ex) {
        log.error("Device not found.", ex);
        return buildErrorResponse("Device not found.", HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleOrderNotFoundException(OrderNotFoundException ex) {
        log.error("Order not found.", ex);
        return buildErrorResponse("Order not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> handleOrderAlreadyExistsException(OrderAlreadyExistsException ex) {
        log.error("Order already exists.", ex);
        return buildErrorResponse("Order already exists.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssignmentConflictException.class)
    public ResponseEntity<ApiResponseDto<?>> handleAssignmentConflictException(AssignmentConflictException ex) {
        log.error("Assignment Conflict: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeviceAssignmentNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleAssignmentConflictException(DeviceAssignmentNotFoundException ex) {
        log.error("Device assignment not found.", ex);
        return buildErrorResponse("Device assignment not found.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlertAssignmentNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleAlertAssignmentConflictException(AlertAssignmentNotFoundException ex) {
        log.error("Alert assignment not found. {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlertAssignmentAmbiguousException.class)
    public ResponseEntity<ApiResponseDto<?>> handleAlertAssignmentAmbiguousException(AlertAssignmentAmbiguousException ex) {
        log.error("Alert assignment ambiguity. {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> handleAlertNotFoundException(AlertNotFoundException ex) {
        log.error("Alert not found. {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid request. {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
