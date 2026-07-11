package com.hgn.sosalert.feature.trekGroup.service;

import com.hgn.sosalert.feature.trekGroup.resource.request.TrekGroupRequestDto;
import com.hgn.sosalert.feature.trekGroup.resource.response.TrekGroupResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrekGroupService {

    TrekGroupResponseDto createTrekGroup(TrekGroupRequestDto trekGroupRequestDto);

    TrekGroupResponseDto getTrekGroupById(Long trekGroupId);

    Page<TrekGroupResponseDto> getAllTrekGroup(Pageable pageable);

    TrekGroupResponseDto updateTrekGroupById(Long trekGroupId, TrekGroupRequestDto trekGroupRequestDto);

    void deactivateTrekGroupById(Long trekGroupId);
}
