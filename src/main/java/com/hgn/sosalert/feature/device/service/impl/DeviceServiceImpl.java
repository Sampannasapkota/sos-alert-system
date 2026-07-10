package com.hgn.sosalert.feature.device.service.impl;

import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.device.exception.DeviceAlreadyExistsException;
import com.hgn.sosalert.feature.device.exception.DeviceNotFoundException;
import com.hgn.sosalert.feature.device.mapper.DeviceMapper;
import com.hgn.sosalert.feature.device.repository.DeviceRepository;
import com.hgn.sosalert.feature.device.resource.request.DeviceRequestDto;
import com.hgn.sosalert.feature.device.resource.response.DeviceResponseDto;
import com.hgn.sosalert.feature.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public DeviceResponseDto createDevice(DeviceRequestDto deviceRequestDto) {
        String deviceCode = deviceRequestDto.getDeviceCode().trim().toUpperCase();

        if (deviceRepository.existsByDeviceCode(deviceCode)) {
            log.error("Device code already exists: {}", deviceCode);
            throw new DeviceAlreadyExistsException("Device code already exists.");
        }

        Device device = Device.builder()
                .deviceCode(deviceCode)
                .displayName(deviceRequestDto.getDisplayName())
                .active(true)
                .build();

        Device savedDevice = deviceRepository.save(device);
        return DeviceMapper.mapToResponse(savedDevice);
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceResponseDto getDeviceById(Long deviceId) {

        Device device = deviceRepository.findByIdAndActiveTrue(deviceId).orElseThrow(() -> {

            log.error("Device not found of id {}", deviceId);
            return new DeviceNotFoundException("Device not found.");
        });
        return DeviceMapper.mapToResponse(device);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceResponseDto> getAllDevice(Pageable pageable) {
        return deviceRepository.findAll(pageable)
                .map(DeviceMapper::mapToResponse);
    }

    @Override
    @Transactional
    public DeviceResponseDto updateDeviceById(Long deviceId, DeviceRequestDto deviceRequestDto) {
        Device device = deviceRepository.findByIdAndActiveTrue(deviceId).orElseThrow(() -> {

            log.error("Device not found of id {}", deviceId);
            return new DeviceNotFoundException("Device not found.");
        });
        String deviceCode = deviceRequestDto.getDeviceCode().trim().toUpperCase();
        if (!device.getDeviceCode().equals(deviceCode) && deviceRepository.existsByDeviceCode(deviceCode)) {
            log.error("Device already exists of code {}", deviceCode);
            throw new DeviceAlreadyExistsException("Device code already exists.");
        }

        device.setDeviceCode(deviceCode);
        device.setDisplayName(deviceRequestDto.getDisplayName());

        Device updatedDevice = deviceRepository.save(device);

        return DeviceMapper.mapToResponse(updatedDevice);
    }

    @Override
    @Transactional
    public void deactivateDeviceById(Long deviceId) {

        Device device = deviceRepository.findByIdAndActiveTrue(deviceId)
                .orElseThrow(() -> {
                    log.error("Device not found for id: {} while deactivating device.", deviceId);
                    return new DeviceNotFoundException("Device not found.");
                });

        device.setActive(false);

        deviceRepository.save(device);

        log.info("Device deactivated successfully of id {}", deviceId);
    }
}
