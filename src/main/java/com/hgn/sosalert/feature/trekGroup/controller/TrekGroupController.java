package com.hgn.sosalert.feature.trekGroup.controller;

import com.hgn.sosalert.feature.trekGroup.resource.request.TrekGroupRequestDto;
import com.hgn.sosalert.feature.trekGroup.resource.response.TrekGroupResponseDto;
import com.hgn.sosalert.feature.trekGroup.service.TrekGroupService;
import com.hgn.sosalert.shared.enums.ResponseStatus;
import com.hgn.sosalert.shared.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trek-groups")
@RequiredArgsConstructor
public class TrekGroupController {

    private final TrekGroupService trekGroupService;

    @PostMapping
    public ApiResponseDto<TrekGroupResponseDto> createTrekGroup(
            @Valid @RequestBody TrekGroupRequestDto trekGroupRequestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trek group created successfully",
                trekGroupService.createTrekGroup(trekGroupRequestDto)
        );
    }

    @GetMapping
    public ApiResponseDto<Page<TrekGroupResponseDto>> getAllTrekGroups(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trek groups fetched successfully",
                trekGroupService.getAllTrekGroup(pageable)
        );
    }

    @GetMapping("/{id}")
    public ApiResponseDto<TrekGroupResponseDto> getTrekGroupById(
            @PathVariable Long id
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trek group fetched successfully",
                trekGroupService.getTrekGroupById(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponseDto<TrekGroupResponseDto> updateTrekGroupById(
            @PathVariable Long id,
            @Valid @RequestBody TrekGroupRequestDto trekGroupRequestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trek group updated successfully",
                trekGroupService.updateTrekGroupById(id, trekGroupRequestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deactivateTrekGroupById(
            @PathVariable Long id
    ) {
        trekGroupService.deactivateTrekGroupById(id);

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trek group deactivated successfully"
        );
    }


}
