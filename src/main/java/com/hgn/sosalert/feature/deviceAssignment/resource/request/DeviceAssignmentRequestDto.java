package com.hgn.sosalert.feature.deviceAssignment.resource.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeviceAssignmentRequestDto {

    @NotNull(message = "Device ID is required")
    private Long deviceId;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Assigned-from timestamp is required")
    private LocalDateTime assignedFrom;

    private LocalDateTime assignedUntil;
}
