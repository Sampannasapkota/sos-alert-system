package com.hgn.sosalert.feature.device.resource.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequestDto {

    @NotBlank(message = "Device code is required")
    @Size(max = 100, message = "Device code must not exceed 100 characters")
    private String deviceCode;

    @Size(max = 150, message = "Display name must not exceed 150 characters")
    private String displayName;
}
