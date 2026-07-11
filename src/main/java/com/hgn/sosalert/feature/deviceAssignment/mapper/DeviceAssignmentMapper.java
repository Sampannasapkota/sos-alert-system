package com.hgn.sosalert.feature.deviceAssignment.mapper;

import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.deviceAssignment.entity.DeviceAssignment;
import com.hgn.sosalert.feature.deviceAssignment.resource.request.DeviceAssignmentRequestDto;
import com.hgn.sosalert.feature.deviceAssignment.resource.response.DeviceAssignmentResponseDto;
import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;

public final class DeviceAssignmentMapper {

    private DeviceAssignmentMapper() {
    }

    public static DeviceAssignment mapToEntity(
            DeviceAssignmentRequestDto requestDto,
            Device device,
            Order order
    ) {
        DeviceAssignment assignment = new DeviceAssignment();

        assignment.setDevice(device);
        assignment.setOrder(order);
        assignment.setAssignedFrom(requestDto.getAssignedFrom());
        assignment.setAssignedUntil(requestDto.getAssignedUntil());
        assignment.setActive(true);

        return assignment;
    }

    public static void mapToExistingEntity(
            DeviceAssignment assignment,
            DeviceAssignmentRequestDto requestDto,
            Device device,
            Order order
    ) {
        assignment.setDevice(device);
        assignment.setOrder(order);
        assignment.setAssignedFrom(requestDto.getAssignedFrom());
        assignment.setAssignedUntil(requestDto.getAssignedUntil());
    }

    public static DeviceAssignmentResponseDto mapToResponse(
            DeviceAssignment assignment
    ) {
        Device device = assignment.getDevice();
        Order order = assignment.getOrder();
        TrekGroup trekGroup = order.getTrekGroup();

        return new DeviceAssignmentResponseDto(
                assignment.getId(),
                assignment.getVersion(),

                device.getId(),
                device.getDeviceCode(),
                device.getDisplayName(),

                order.getId(),
                order.getOrderReference(),
                order.getTrekName(),

                trekGroup.getId(),
                trekGroup.getGroupCode(),
                trekGroup.getGroupName(),

                assignment.getAssignedFrom(),
                assignment.getAssignedUntil(),
                assignment.getActive(),

                assignment.getCreatedAt(),
                assignment.getModifiedAt()
        );
    }
}
