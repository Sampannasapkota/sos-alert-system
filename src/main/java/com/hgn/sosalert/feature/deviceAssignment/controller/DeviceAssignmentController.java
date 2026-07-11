package com.hgn.sosalert.feature.deviceAssignment.controller;

import com.hgn.sosalert.feature.deviceAssignment.resource.request.DeviceAssignmentRequestDto;
import com.hgn.sosalert.feature.deviceAssignment.resource.response.DeviceAssignmentResponseDto;
import com.hgn.sosalert.feature.deviceAssignment.service.DeviceAssignmentService;
import com.hgn.sosalert.shared.enums.ResponseStatus;
import com.hgn.sosalert.shared.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/device-assignments")
@RequiredArgsConstructor
public class DeviceAssignmentController {
    private final DeviceAssignmentService assignmentService;

    @PostMapping
    public ApiResponseDto<DeviceAssignmentResponseDto>
    createAssignment(
            @Valid @RequestBody
            DeviceAssignmentRequestDto requestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device assignment created successfully",
                assignmentService.createAssignment(requestDto)
        );
    }

    @GetMapping
    public ApiResponseDto<Page<DeviceAssignmentResponseDto>>
    getAllAssignments(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device assignments fetched successfully",
                assignmentService.getAllAssignments(pageable)
        );
    }

    @GetMapping("/{id}")
    public ApiResponseDto<DeviceAssignmentResponseDto>
    getAssignmentById(@PathVariable Long id) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device assignment fetched successfully",
                assignmentService.getAssignmentById(id)
        );
    }

    @GetMapping("/device/{deviceId}")
    public ApiResponseDto<Page<DeviceAssignmentResponseDto>>
    getAssignmentsByDeviceId(
            @PathVariable Long deviceId,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "assignedFrom",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device assignments fetched successfully",
                assignmentService.getAssignmentsByDeviceId(
                        deviceId,
                        pageable
                )
        );
    }

    @GetMapping("/order/{orderId}")
    public ApiResponseDto<Page<DeviceAssignmentResponseDto>>
    getAssignmentsByOrderId(
            @PathVariable Long orderId,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "assignedFrom",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Order assignments fetched successfully",
                assignmentService.getAssignmentsByOrderId(
                        orderId,
                        pageable
                )
        );
    }

    @PutMapping("/{id}")
    public ApiResponseDto<DeviceAssignmentResponseDto>
    updateAssignmentById(
            @PathVariable Long id,
            @Valid @RequestBody
            DeviceAssignmentRequestDto requestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device assignment updated successfully",
                assignmentService.updateAssignmentById(
                        id,
                        requestDto
                )
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deactivateAssignmentById(
            @PathVariable Long id
    ) {
        assignmentService.deactivateAssignmentById(id);

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device assignment deactivated successfully"
        );
    }
}
