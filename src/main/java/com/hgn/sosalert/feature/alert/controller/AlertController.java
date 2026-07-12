package com.hgn.sosalert.feature.alert.controller;

import com.hgn.sosalert.feature.alert.resource.request.AlertRequestDto;
import com.hgn.sosalert.feature.alert.resource.response.AlertResponseDto;
import com.hgn.sosalert.feature.alert.service.AlertService;
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
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ApiResponseDto<AlertResponseDto> receiveAlert(
            @Valid @RequestBody AlertRequestDto requestDto
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "SOS alert received successfully",
                alertService.receiveAlert(requestDto)
        );
    }

    @GetMapping("/{id}")
    public ApiResponseDto<AlertResponseDto> getAlertById(
            @PathVariable Long id
    ) {
        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "SOS alert fetched successfully",
                alertService.getAlertById(id)
        );
    }

    @GetMapping
    public ApiResponseDto<Page<AlertResponseDto>> getAllAlerts(
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
                "SOS alerts fetched successfully",
                alertService.getAllAlerts(pageable)
        );
    }
}
