package com.hgn.sosalert.feature.alert.service.serviceImpl;

import com.hgn.sosalert.feature.alert.entity.Alert;
import com.hgn.sosalert.feature.alert.exception.AlertAssignmentAmbiguousException;
import com.hgn.sosalert.feature.alert.exception.AlertAssignmentNotFoundException;
import com.hgn.sosalert.feature.alert.exception.AlertNotFoundException;
import com.hgn.sosalert.feature.alert.mapper.AlertMapper;
import com.hgn.sosalert.feature.alert.repository.AlertRepository;
import com.hgn.sosalert.feature.alert.resource.request.AlertRequestDto;
import com.hgn.sosalert.feature.alert.resource.response.AlertResponseDto;
import com.hgn.sosalert.feature.alert.service.AlertService;
import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.device.exception.DeviceNotFoundException;
import com.hgn.sosalert.feature.device.repository.DeviceRepository;
import com.hgn.sosalert.feature.deviceAssignment.entity.DeviceAssignment;
import com.hgn.sosalert.feature.deviceAssignment.repository.DeviceAssignmentRepository;
import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private static final long DEDUPLICATION_WINDOW_MINUTES = 2;

    private final AlertRepository alertRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceAssignmentRepository assignmentRepository;

    @Override
    @Transactional
    public AlertResponseDto receiveAlert(AlertRequestDto requestDto) {

        Device device = findActiveDeviceById(
                requestDto.getDeviceId()
        );

        validateAlertTimestamp(requestDto.getTimestamp());

        DeviceAssignment assignment = resolveAssignment(
                device.getId(),
                requestDto.getTimestamp()
        );

        Optional<Alert> duplicateAlert =
                findDuplicateAlert(
                        device.getId(),
                        requestDto.getTimestamp()
                );

        if (duplicateAlert.isPresent()) {
            Alert existingAlert = duplicateAlert.get();
            return AlertMapper.mapToResponse(existingAlert);
        }

        Order order = assignment.getOrder();

        TrekGroup trekGroup = order.getTrekGroup();

        Alert alert = AlertMapper.mapToEntity(
                requestDto,
                device,
                order,
                trekGroup
        );

        Alert savedAlert = alertRepository.save(alert);

        return AlertMapper.mapToResponse(savedAlert);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponseDto getAlertById(Long alertId) {
        return AlertMapper.mapToResponse(
                findAlertById(alertId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponseDto> getAllAlerts(Pageable pageable) {
        return alertRepository.findAll(pageable)
                .map(AlertMapper::mapToResponse);
    }

    private DeviceAssignment resolveAssignment(
            Long deviceId,
            LocalDateTime alertTimestamp
    ) {
        List<DeviceAssignment> matchingAssignments =
                assignmentRepository.findMatchingAssignments(
                        deviceId,
                        alertTimestamp
                );

        if (matchingAssignments.isEmpty()) {
            log.error(
                    "No device assignment found for SOS alert. deviceId={}, alertTimestamp={}",
                    deviceId,
                    alertTimestamp
            );

            throw new AlertAssignmentNotFoundException(
                    "No active device assignment was found for the alert timestamp."
            );
        }

        if (matchingAssignments.size() > 1) {
            log.error(
                    "Ambiguous device assignment for SOS alert. deviceId={}, alertTimestamp={}, matchingAssignments={}",
                    deviceId,
                    alertTimestamp,
                    matchingAssignments.size()
            );

            throw new AlertAssignmentAmbiguousException(
                    "Multiple device assignments match the alert timestamp."
            );
        }

        return matchingAssignments.getFirst();
    }

    private Optional<Alert> findDuplicateAlert(
            Long deviceId,
            LocalDateTime alertTimestamp
    ) {
        LocalDateTime windowStart =
                alertTimestamp.minusMinutes(
                        DEDUPLICATION_WINDOW_MINUTES
                );

        LocalDateTime windowEnd =
                alertTimestamp.plusMinutes(
                        DEDUPLICATION_WINDOW_MINUTES
                );

        return alertRepository
                .findFirstByDeviceIdAndAlertTimestampBetweenOrderByAlertTimestampDesc(
                        deviceId,
                        windowStart,
                        windowEnd
                );
    }

    private void validateAlertTimestamp(
            LocalDateTime alertTimestamp
    ) {
        if (alertTimestamp.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Alert timestamp cannot be in the future."
            );
        }
    }

    private Device findActiveDeviceById(Long deviceId) {
        return deviceRepository.findByIdAndActiveTrue(deviceId)
                .orElseThrow(() -> {
                    log.error(
                            "Active device not found. deviceId={}",
                            deviceId
                    );

                    return new DeviceNotFoundException(
                            "Device not found."
                    );
                });
    }

    private Alert findAlertById(Long alertId) {
        return alertRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.error(
                            "SOS alert not found. alertId={}",
                            alertId
                    );

                    return new AlertNotFoundException(
                            "SOS alert not found."
                    );
                });
    }
}
