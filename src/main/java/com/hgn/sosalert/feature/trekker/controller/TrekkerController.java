package com.hgn.sosalert.feature.trekker.controller;

import com.hgn.sosalert.feature.trekker.resource.request.TrekkerRequestDto;
import com.hgn.sosalert.feature.trekker.resource.response.TrekkerResponseDto;
import com.hgn.sosalert.feature.trekker.service.TrekkerService;
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
@RequestMapping("/api/v1/trekkers")
@RequiredArgsConstructor
public class TrekkerController {
    private final TrekkerService trekkerService;

    @PostMapping
    public ApiResponseDto<TrekkerResponseDto> createTrekker(
            @Valid @RequestBody
            TrekkerRequestDto trekkerRequestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trekker created successfully",
                trekkerService.createTrekker(trekkerRequestDto)
        );
    }

    @GetMapping
    public ApiResponseDto<Page<TrekkerResponseDto>> getAllTrekkers(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trekkers fetched successfully",
                trekkerService.getAllTrekkers(pageable)
        );
    }

    @GetMapping("/{id}")
    public ApiResponseDto<TrekkerResponseDto> getTrekkerById(
            @PathVariable Long id
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trekker fetched successfully",
                trekkerService.getTrekkerById(id)
        );
    }

    @GetMapping("/trek-group/{trekGroupId}")
    public ApiResponseDto<Page<TrekkerResponseDto>>
    getTrekkersByTrekGroupId(
            @PathVariable Long trekGroupId,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trekkers fetched successfully",
                trekkerService.getTrekkersByTrekGroupId(
                        trekGroupId,
                        pageable
                )
        );
    }

    @PutMapping("/{id}")
    public ApiResponseDto<TrekkerResponseDto> updateTrekkerById(
            @PathVariable Long id,
            @Valid @RequestBody
            TrekkerRequestDto trekkerRequestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trekker updated successfully",
                trekkerService.updateTrekkerById(
                        id,
                        trekkerRequestDto
                )
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deactivateTrekkerById(
            @PathVariable Long id
    ) {
        trekkerService.deactivateTrekkerById(id);

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Trekker deactivated successfully"
        );
    }


}
