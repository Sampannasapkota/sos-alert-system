package com.hgn.sosalert.feature.deviceAssignment.resource.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeviceAssignmentResponseDto {

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

    private LocalDateTime assignedFrom;
    private LocalDateTime assignedUntil;
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
