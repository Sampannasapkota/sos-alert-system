package com.hgn.sosalert.feature.trekker.service.impl;

import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import com.hgn.sosalert.feature.trekGroup.exception.TrekGroupNotFoundException;
import com.hgn.sosalert.feature.trekGroup.repository.TrekGroupRepository;
import com.hgn.sosalert.feature.trekker.entity.Trekker;
import com.hgn.sosalert.feature.trekker.exception.TrekkerNotFoundException;
import com.hgn.sosalert.feature.trekker.mapper.TrekkerMapper;
import com.hgn.sosalert.feature.trekker.repository.TrekkerRepository;
import com.hgn.sosalert.feature.trekker.resource.request.TrekkerRequestDto;
import com.hgn.sosalert.feature.trekker.resource.response.TrekkerResponseDto;
import com.hgn.sosalert.feature.trekker.service.TrekkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrekkerServiceImpl implements TrekkerService {

    private final TrekkerRepository trekkerRepository;
    private final TrekGroupRepository trekGroupRepository;

    @Override
    @Transactional
    public TrekkerResponseDto createTrekker(TrekkerRequestDto trekkerRequestDto) {
        TrekGroup trekGroup = findActiveTrekGroupById(trekkerRequestDto.getTrekGroupId());

        Trekker trekker = TrekkerMapper.mapToEntity(trekkerRequestDto, trekGroup);

        Trekker savedTrekker = trekkerRepository.save(trekker);

        return TrekkerMapper.mapToResponse(savedTrekker);
    }

    @Override
    @Transactional(readOnly = true)
    public TrekkerResponseDto getTrekkerById(Long trekkerId) {

        Trekker trekker = findActiveTrekkerById(trekkerId);

        return TrekkerMapper.mapToResponse(trekker);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrekkerResponseDto> getAllTrekkers(Pageable pageable) {
        return trekkerRepository.findAllByActiveTrue(pageable)
                .map(TrekkerMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrekkerResponseDto> getTrekkersByTrekGroupId(Long trekGroupId, Pageable pageable) {
        findActiveTrekGroupById(trekGroupId);
        return trekkerRepository.findAllByTrekGroupIdAndActiveTrue(trekGroupId, pageable)
                .map(TrekkerMapper::mapToResponse);
    }

    @Override
    @Transactional
    public TrekkerResponseDto updateTrekkerById(Long trekkerId, TrekkerRequestDto trekkerRequestDto) {
        Trekker trekker = findActiveTrekkerById(trekkerId);

        TrekGroup trekGroup = findActiveTrekGroupById(trekkerRequestDto.getTrekGroupId());
        TrekkerMapper.mapToExistingEntity(trekker, trekkerRequestDto, trekGroup);
        return TrekkerMapper.mapToResponse(trekker);
    }

    @Override
    @Transactional
    public void deactivateTrekkerById(Long trekkerId) {
        Trekker trekker = findActiveTrekkerById(trekkerId);
        trekker.setActive(false);
    }

    private Trekker findActiveTrekkerById(Long trekkerId) {
        return trekkerRepository.findByIdAndActiveTrue(trekkerId)
                .orElseThrow(() -> {
                    log.error("Active trekker not found. id {}", trekkerId);

                    return new TrekkerNotFoundException("Trekker not found.");
                });
    }

    private TrekGroup findActiveTrekGroupById(Long trekGroupId) {
        return trekGroupRepository.findByIdAndActiveTrue(trekGroupId)
                .orElseThrow(() -> {
                    log.error("Active trek group not found. id={}", trekGroupId);

                    return new TrekGroupNotFoundException("Trek group not found.");
                });
    }
}
