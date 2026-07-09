package com.hgn.sosalert.feature.device.service;

import com.hgn.sosalert.feature.device.resource.request.DeviceRequestDto;
import com.hgn.sosalert.feature.device.resource.response.DeviceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {

    DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto);

    DeviceResponseDto getDeviceById(Long deviceId);

    Page<DeviceResponseDto> getAllDevice(Pageable pageable);

    DeviceResponseDto updateDeviceById(Long deviceId, DeviceRequestDto deviceRequestDto);

    void deactivateDeviceById(Long deviceId);

}
