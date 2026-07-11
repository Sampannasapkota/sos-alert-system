package com.hgn.sosalert.feature.trekGroup.resource.request;

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
public class TrekGroupRequestDto {

    @NotBlank(message = "Group code is required")
    @Size(max = 50, message = "Group code must not exceed 50 characters")
    private String groupCode;

    @NotBlank(message = "Group name is required")
    @Size(max = 150, message = "Group name must not exceed 150 characters")
    private String groupName;
}
