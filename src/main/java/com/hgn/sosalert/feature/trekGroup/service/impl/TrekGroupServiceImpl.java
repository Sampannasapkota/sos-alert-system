package com.hgn.sosalert.feature.trekGroup.service.impl;

import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import com.hgn.sosalert.feature.trekGroup.exception.TrekGroupAlreadyExistsException;
import com.hgn.sosalert.feature.trekGroup.exception.TrekGroupNotFoundException;
import com.hgn.sosalert.feature.trekGroup.mapper.TrekGroupMapper;
import com.hgn.sosalert.feature.trekGroup.repository.TrekGroupRepository;
import com.hgn.sosalert.feature.trekGroup.resource.request.TrekGroupRequestDto;
import com.hgn.sosalert.feature.trekGroup.resource.response.TrekGroupResponseDto;
import com.hgn.sosalert.feature.trekGroup.service.TrekGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrekGroupServiceImpl implements TrekGroupService {
    private final TrekGroupRepository trekGroupRepository;

    @Override
    @Transactional
    public TrekGroupResponseDto createTrekGroup(TrekGroupRequestDto trekGroupRequestDto) {

        String groupCode = normalizeGroupCode(trekGroupRequestDto.getGroupCode());
        if (trekGroupRepository.existsByGroupCode(groupCode)) {
            log.error("Trek group code already exists: {}", groupCode);
            throw new TrekGroupAlreadyExistsException("Trek group code already exists.");
        }

        TrekGroup trekGroup = TrekGroupMapper.mapToEntity(trekGroupRequestDto);

        trekGroup.setGroupCode(groupCode);
        trekGroup.setGroupName(trekGroupRequestDto.getGroupName().trim());
        trekGroup.setActive(true);

        TrekGroup savedTrekGroup = trekGroupRepository.save(trekGroup);
        return TrekGroupMapper.mapToResponse(savedTrekGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public TrekGroupResponseDto getTrekGroupById(Long trekGroupId) {
        TrekGroup trekGroup = trekGroupRepository.findByIdAndActiveTrue(trekGroupId).orElseThrow(() -> {
            log.error("Trek group not found of id {}", trekGroupId);
            throw new TrekGroupNotFoundException("Trek Group not found.");
        });
        return TrekGroupMapper.mapToResponse(trekGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrekGroupResponseDto> getAllTrekGroup(Pageable pageable) {

        return trekGroupRepository.findAllByActiveTrue(pageable)
                .map(TrekGroupMapper::mapToResponse);
    }

    @Override
    @Transactional
    public TrekGroupResponseDto updateTrekGroupById(Long trekGroupId, TrekGroupRequestDto trekGroupRequestDto) {
        TrekGroup trekGroup = trekGroupRepository.findByIdAndActiveTrue(trekGroupId).orElseThrow(() -> {
            log.error("Trek group not found of id {}", trekGroupId);
            throw new TrekGroupNotFoundException("Trek group not found.");
        });

        String groupCode = normalizeGroupCode(trekGroupRequestDto.getGroupCode());

        if (trekGroupRepository.existsByGroupCodeAndIdNot(groupCode, trekGroupId)) {
            log.error("Trek group code already exists: {}", groupCode);
            throw new TrekGroupAlreadyExistsException("Trek group code already exists.");
        }
        trekGroup.setGroupCode(groupCode);
        trekGroup.setGroupName(trekGroupRequestDto.getGroupName().trim());
        TrekGroup updatedTrekGroup = trekGroupRepository.save(trekGroup);

        return TrekGroupMapper.mapToResponse(updatedTrekGroup);
    }

    @Override
    @Transactional
    public void deactivateTrekGroupById(Long trekGroupId) {
        TrekGroup trekGroup = trekGroupRepository.findByIdAndActiveTrue(trekGroupId).orElseThrow(() -> {
            log.error("Trek group not found of id {}", trekGroupId);
            throw new TrekGroupNotFoundException("Trek Group not found.");
        });
        trekGroup.setActive(false);
        log.info("Trek group deactivated successfully of id {}", trekGroupId);

    }

    private String normalizeGroupCode(String groupCode) {
        return groupCode.trim().toUpperCase();
    }
}
