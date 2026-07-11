package com.hgn.sosalert.feature.deviceAssignment.service.impl;

import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.device.exception.DeviceNotFoundException;
import com.hgn.sosalert.feature.device.repository.DeviceRepository;
import com.hgn.sosalert.feature.deviceAssignment.entity.DeviceAssignment;
import com.hgn.sosalert.feature.deviceAssignment.exception.AssignmentConflictException;
import com.hgn.sosalert.feature.deviceAssignment.exception.DeviceAssignmentNotFoundException;
import com.hgn.sosalert.feature.deviceAssignment.mapper.DeviceAssignmentMapper;
import com.hgn.sosalert.feature.deviceAssignment.repository.DeviceAssignmentRepository;
import com.hgn.sosalert.feature.deviceAssignment.resource.request.DeviceAssignmentRequestDto;
import com.hgn.sosalert.feature.deviceAssignment.resource.response.DeviceAssignmentResponseDto;
import com.hgn.sosalert.feature.deviceAssignment.service.DeviceAssignmentService;
import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.order.enums.OrderStatus;
import com.hgn.sosalert.feature.order.exception.OrderNotFoundException;
import com.hgn.sosalert.feature.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceAssignmentServiceImpl implements DeviceAssignmentService {

    private static final LocalDateTime FAR_FUTURE =
            LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    private final DeviceAssignmentRepository assignmentRepository;
    private final DeviceRepository deviceRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public DeviceAssignmentResponseDto createAssignment(DeviceAssignmentRequestDto requestDto) {
        validateAssignmentDates(requestDto);

        Device device = findActiveDeviceById(requestDto.getDeviceId());
        Order order = findActiveOrderById(requestDto.getOrderId());

        validateOrderStatus(order);
        validateAssignmentWithinOrderPeriod(requestDto, order);
        validateNoOverlap(
                device.getId(),
                requestDto.getAssignedFrom(),
                requestDto.getAssignedUntil(),
                null
        );

        DeviceAssignment assignment =
                DeviceAssignmentMapper.mapToEntity(
                        requestDto,
                        device,
                        order
                );

        DeviceAssignment savedAssignment =
                assignmentRepository.save(assignment);

        log.info(
                "Device assignment created. assignmentId={}, deviceId={}, orderId={}",
                savedAssignment.getId(),
                device.getId(),
                order.getId()
        );

        return DeviceAssignmentMapper.mapToResponse(savedAssignment);
    }


    @Override
    @Transactional(readOnly = true)
    public DeviceAssignmentResponseDto getAssignmentById(Long assignmentId) {
        return DeviceAssignmentMapper.mapToResponse(
                findActiveAssignmentById(assignmentId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceAssignmentResponseDto> getAllAssignments(Pageable pageable) {
        return assignmentRepository.findAllByActiveTrue(pageable)
                .map(DeviceAssignmentMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceAssignmentResponseDto> getAssignmentsByDeviceId(Long deviceId, Pageable pageable) {
        findActiveDeviceById(deviceId);

        return assignmentRepository
                .findAllByDeviceIdAndActiveTrue(deviceId, pageable)
                .map(DeviceAssignmentMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceAssignmentResponseDto> getAssignmentsByOrderId(Long orderId, Pageable pageable) {
        findActiveOrderById(orderId);
        return assignmentRepository
                .findAllByOrderIdAndActiveTrue(orderId, pageable)
                .map(DeviceAssignmentMapper::mapToResponse);
    }

    @Override
    @Transactional
    public DeviceAssignmentResponseDto updateAssignmentById(Long assignmentId, DeviceAssignmentRequestDto requestDto) {
        validateAssignmentDates(requestDto);

        DeviceAssignment assignment =
                findActiveAssignmentById(assignmentId);

        Device device = findActiveDeviceById(requestDto.getDeviceId());
        Order order = findActiveOrderById(requestDto.getOrderId());

        validateOrderStatus(order);
        validateAssignmentWithinOrderPeriod(requestDto, order);
        validateNoOverlap(
                device.getId(),
                requestDto.getAssignedFrom(),
                requestDto.getAssignedUntil(),
                assignmentId
        );

        DeviceAssignmentMapper.mapToExistingEntity(
                assignment,
                requestDto,
                device,
                order
        );

        log.info(
                "Device assignment updated. assignmentId={}, deviceId={}, orderId={}",
                assignmentId,
                device.getId(),
                order.getId()
        );

        return DeviceAssignmentMapper.mapToResponse(assignment);
    }

    @Override
    @Transactional
    public void deactivateAssignmentById(Long assignmentId) {

        DeviceAssignment assignment =
                findActiveAssignmentById(assignmentId);

        assignment.setActive(false);

        if (assignment.getAssignedUntil() == null) {
            assignment.setAssignedUntil(LocalDateTime.now());
        }

        log.info(
                "Device assignment deactivated. assignmentId={}",
                assignmentId
        );

    }

    private void validateAssignmentDates(
            DeviceAssignmentRequestDto requestDto
    ) {
        if (requestDto.getAssignedUntil() != null
                && requestDto.getAssignedUntil()
                .isBefore(requestDto.getAssignedFrom())) {
            throw new AssignmentConflictException(
                    "Assigned-until cannot be before assigned-from."
            );
        }
    }

    private void validateNoOverlap(
            Long deviceId,
            LocalDateTime assignedFrom,
            LocalDateTime assignedUntil,
            Long excludedAssignmentId
    ) {
        LocalDateTime effectiveEnd =
                assignedUntil != null ? assignedUntil : FAR_FUTURE;

        List<DeviceAssignment> overlaps =
                assignmentRepository.findOverlappingAssignments(
                        deviceId,
                        assignedFrom,
                        effectiveEnd,
                        excludedAssignmentId
                );

        if (!overlaps.isEmpty()) {
            throw new AssignmentConflictException(
                    "Device already has an overlapping assignment."
            );
        }
    }

    private void validateOrderStatus(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.COMPLETED) {
            throw new AssignmentConflictException(
                    "A device cannot be assigned to a completed or cancelled order."
            );
        }
    }

    private void validateAssignmentWithinOrderPeriod(
            DeviceAssignmentRequestDto requestDto,
            Order order
    ) {
        LocalDateTime orderStart =
                order.getStartDate().atStartOfDay();

        LocalDateTime orderEnd =
                order.getEndDate().atTime(23, 59, 59);

        if (requestDto.getAssignedFrom().isBefore(orderStart)) {
            throw new AssignmentConflictException(
                    "Assignment cannot start before the order start date."
            );
        }

        if (requestDto.getAssignedUntil() != null
                && requestDto.getAssignedUntil().isAfter(orderEnd)) {
            throw new AssignmentConflictException(
                    "Assignment cannot end after the order end date."
            );
        }
    }

    private DeviceAssignment findActiveAssignmentById(
            Long assignmentId
    ) {
        return assignmentRepository
                .findByIdAndActiveTrue(assignmentId)
                .orElseThrow(() -> {
                    log.warn(
                            "Active device assignment not found. id={}",
                            assignmentId
                    );

                    return new DeviceAssignmentNotFoundException(
                            "Device assignment not found."
                    );
                });
    }

    private Device findActiveDeviceById(Long deviceId) {
        return deviceRepository.findByIdAndActiveTrue(deviceId)
                .orElseThrow(() -> {
                    log.warn(
                            "Active device not found. id={}",
                            deviceId
                    );

                    return new DeviceNotFoundException(
                            "Device not found."
                    );
                });
    }

    private Order findActiveOrderById(Long orderId) {
        return orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> {
                    log.warn(
                            "Active order not found. id={}",
                            orderId
                    );

                    return new OrderNotFoundException(
                            "Order not found."
                    );
                });
    }
}
