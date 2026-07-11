package com.hgn.sosalert.feature.trekker.resource.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrekkerRequestDto {

    @NotBlank(message = "Full name is required")
    @Size(
            max = 150,
            message = "Full name must not exceed 150 characters"
    )
    private String fullName;

    @Size(
            max = 30,
            message = "Phone number must not exceed 30 characters"
    )
    private String phoneNumber;

    @Size(
            max = 100,
            message = "Nationality must not exceed 100 characters"
    )
    private String nationality;

    @Size(
            max = 30,
            message = "Emergency contact must not exceed 30 characters"
    )
    private String emergencyContact;

    @NotNull(message = "Trek group ID is required")
    private Long trekGroupId;
}
