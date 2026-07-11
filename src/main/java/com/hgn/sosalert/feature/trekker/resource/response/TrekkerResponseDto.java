package com.hgn.sosalert.feature.trekker.resource.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TrekkerResponseDto {

    private Long id;
    private Long version;

    private String fullName;
    private String phoneNumber;
    private String nationality;
    private String emergencyContact;
    private Boolean active;

    private Long trekGroupId;
    private String trekGroupCode;
    private String trekGroupName;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
