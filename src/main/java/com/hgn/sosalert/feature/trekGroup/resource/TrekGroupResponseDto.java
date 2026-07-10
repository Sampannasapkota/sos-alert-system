package com.hgn.sosalert.feature.trekGroup.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrekGroupResponseDto {
    private Long id;
    private Long version;
    private String groupCode;
    private String groupName;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
