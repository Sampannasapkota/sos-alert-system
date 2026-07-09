package com.hgn.sosalert.feature.device.resource.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDto {

    private Long id;
    private Long version;
    private String deviceId;
    private String displayName;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
