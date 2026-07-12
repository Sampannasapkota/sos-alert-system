package com.hgn.sosalert.feature.alert.resource.response;

import com.hgn.sosalert.feature.alert.enums.AlertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AlertResponseDto {

    private Long id;
    private Long version;

    private Long deviceId;
    private String deviceCode;
    private String deviceDisplayName;

    private Long orderId;
    private String orderReference;
    private String trekName;

    private Long trekGroupId;
    private String trekGroupCode;
    private String trekGroupName;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime alertTimestamp;

    private AlertStatus status;

    private String claimedBy;
    private LocalDateTime claimedAt;
    private LocalDateTime escalatedAt;
    private LocalDateTime resolvedAt;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
