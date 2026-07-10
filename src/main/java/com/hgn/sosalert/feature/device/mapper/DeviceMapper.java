package com.hgn.sosalert.feature.device.mapper;

import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.device.resource.request.DeviceRequestDto;
import com.hgn.sosalert.feature.device.resource.response.DeviceResponseDto;

public class DeviceMapper {
    public static DeviceResponseDto MapToResponse(Device device) {
        return new DeviceResponseDto(
                device.getId(),
                device.getVersion(),
                device.getDeviceCode(),
                device.getDisplayName(),
                device.getActive(),
                device.getCreatedAt(),
                device.getModifiedAt()
        );
    }

    public static Device MapToDevice(DeviceRequestDto requestDto) {
        Device device = new Device();
        device.setDeviceCode(requestDto.getDeviceCode());
        device.setDisplayName(requestDto.getDisplayName());
        return device;
    }
}
