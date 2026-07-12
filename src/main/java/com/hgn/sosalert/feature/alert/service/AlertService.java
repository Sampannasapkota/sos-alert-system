package com.hgn.sosalert.feature.alert.service;

import com.hgn.sosalert.feature.alert.resource.request.AlertRequestDto;
import com.hgn.sosalert.feature.alert.resource.response.AlertResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertService {
    AlertResponseDto receiveAlert(
            AlertRequestDto requestDto
    );

    AlertResponseDto getAlertById(Long alertId);

    Page<AlertResponseDto> getAllAlerts(
            Pageable pageable
    );
}
