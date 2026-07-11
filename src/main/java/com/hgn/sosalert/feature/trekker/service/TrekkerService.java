package com.hgn.sosalert.feature.trekker.service;

import com.hgn.sosalert.feature.trekker.resource.request.TrekkerRequestDto;
import com.hgn.sosalert.feature.trekker.resource.response.TrekkerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrekkerService {

    TrekkerResponseDto createTrekker(
            TrekkerRequestDto trekkerRequestDto
    );

    TrekkerResponseDto getTrekkerById(Long trekkerId);

    Page<TrekkerResponseDto> getAllTrekkers(Pageable pageable);

    Page<TrekkerResponseDto> getTrekkersByTrekGroupId(
            Long trekGroupId,
            Pageable pageable
    );

    TrekkerResponseDto updateTrekkerById(
            Long trekkerId,
            TrekkerRequestDto trekkerRequestDto
    );

    void deactivateTrekkerById(Long trekkerId);
}
