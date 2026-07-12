package com.hgn.sosalert.feature.alert.resource.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertClaimRequestDto {

    @NotBlank(message = "Coordinator name is required")
    private String coordinatorName;
}
