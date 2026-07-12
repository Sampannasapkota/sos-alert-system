package com.hgn.sosalert.feature.alert.resource.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AlertRequestDto {

    @NotNull(message = "Device ID is required")
    private Long deviceId;

    @NotNull(message = "Latitude is required")
    @DecimalMin(
            value = "-90.0",
            message = "Latitude must be at least -90"
    )
    @DecimalMax(
            value = "90.0",
            message = "Latitude must not exceed 90"
    )
    @Schema(example = "27.9881000")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(
            value = "-180.0",
            message = "Longitude must be at least -180"
    )
    @DecimalMax(
            value = "180.0",
            message = "Longitude must not exceed 180"
    )
    @Schema(example = "86.9250000")
    private BigDecimal longitude;

    @NotNull(message = "Alert timestamp is required")
    @Schema(
            description = "Timestamp reported by the device, without timezone",
            example = "2026-07-15T10:30:00"
    )
    private LocalDateTime timestamp;

}
