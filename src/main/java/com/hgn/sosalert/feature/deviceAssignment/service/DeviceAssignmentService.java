package com.hgn.sosalert.feature.deviceAssignment.service;

import com.hgn.sosalert.feature.deviceAssignment.resource.request.DeviceAssignmentRequestDto;
import com.hgn.sosalert.feature.deviceAssignment.resource.response.DeviceAssignmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceAssignmentService {

    DeviceAssignmentResponseDto createAssignment(
            DeviceAssignmentRequestDto requestDto
    );

    DeviceAssignmentResponseDto getAssignmentById(Long assignmentId);

    Page<DeviceAssignmentResponseDto> getAllAssignments(
            Pageable pageable
    );

    Page<DeviceAssignmentResponseDto> getAssignmentsByDeviceId(
            Long deviceId,
            Pageable pageable
    );

    Page<DeviceAssignmentResponseDto> getAssignmentsByOrderId(
            Long orderId,
            Pageable pageable
    );

    DeviceAssignmentResponseDto updateAssignmentById(
            Long assignmentId,
            DeviceAssignmentRequestDto requestDto
    );

    void deactivateAssignmentById(Long assignmentId);
}
