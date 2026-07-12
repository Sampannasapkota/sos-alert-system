package com.hgn.sosalert.feature.alert.mapper;

import com.hgn.sosalert.feature.alert.entity.Alert;
import com.hgn.sosalert.feature.alert.resource.request.AlertRequestDto;
import com.hgn.sosalert.feature.alert.resource.response.AlertResponseDto;
import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;

public final class AlertMapper {

    private AlertMapper() {
    }

    public static Alert mapToEntity(
            AlertRequestDto requestDto,
            Device device,
            Order order,
            TrekGroup trekGroup
    ) {
        return Alert.builder()
                .device(device)
                .order(order)
                .trekGroup(trekGroup)
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .alertTimestamp(requestDto.getTimestamp())
                .build();
    }

    public static AlertResponseDto mapToResponse(
            Alert alert
    ) {
        Device device = alert.getDevice();
        Order order = alert.getOrder();
        TrekGroup trekGroup = alert.getTrekGroup();

        return new AlertResponseDto(
                alert.getId(),
                alert.getVersion(),

                device.getId(),
                device.getDeviceCode(),
                device.getDisplayName(),

                order.getId(),
                order.getOrderReference(),
                order.getTrekName(),

                trekGroup != null ? trekGroup.getId() : null,
                trekGroup != null ? trekGroup.getGroupCode() : null,
                trekGroup != null ? trekGroup.getGroupName() : null,

                alert.getLatitude(),
                alert.getLongitude(),
                alert.getAlertTimestamp(),

                alert.getStatus(),

                alert.getClaimedBy(),
                alert.getClaimedAt(),
                alert.getEscalatedAt(),
                alert.getResolvedAt(),

                alert.getCreatedAt(),
                alert.getModifiedAt()
        );
    }
}

