package com.hgn.sosalert.feature.device.controller;

import com.hgn.sosalert.feature.device.resource.request.DeviceRequestDto;
import com.hgn.sosalert.feature.device.resource.response.DeviceResponseDto;
import com.hgn.sosalert.feature.device.service.DeviceService;
import com.hgn.sosalert.shared.enums.ResponseStatus;
import com.hgn.sosalert.shared.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PostMapping("/device")
    public ApiResponseDto<DeviceResponseDto> createDevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) {

        return new ApiResponseDto<>(ResponseStatus.SUCCESS.value,
                "Device Created Successfully.",
                deviceService.createDevice(deviceRequestDto));
    }

    @GetMapping
    public ApiResponseDto<Page<DeviceResponseDto>> getAllDevices(Pageable pageable) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Devices fetched successfully",
                deviceService.getAllDevice(pageable)
        );
    }

    @GetMapping("/{id}")
    public ApiResponseDto<DeviceResponseDto> getDeviceById(@PathVariable Long id) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device fetched successfully",
                deviceService.getDeviceById(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponseDto<DeviceResponseDto> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDto deviceRequestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device updated successfully",
                deviceService.updateDeviceById(id, deviceRequestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deactivateDeviceById(@PathVariable Long id) {
        deviceService.deactivateDeviceById(id);

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Device deleted successfully"
        );
    }


}
