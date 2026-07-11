package com.hgn.sosalert.feature.trekker.mapper;

import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import com.hgn.sosalert.feature.trekker.entity.Trekker;
import com.hgn.sosalert.feature.trekker.resource.request.TrekkerRequestDto;
import com.hgn.sosalert.feature.trekker.resource.response.TrekkerResponseDto;

public final class TrekkerMapper {

    private TrekkerMapper() {
    }

    public static Trekker mapToEntity(
            TrekkerRequestDto requestDto,
            TrekGroup trekGroup
    ) {
        Trekker trekker = new Trekker();

        trekker.setFullName(requestDto.getFullName().trim());
        trekker.setPhoneNumber(normalizeNullable(requestDto.getPhoneNumber()));
        trekker.setNationality(normalizeNullable(requestDto.getNationality()));
        trekker.setEmergencyContact(
                normalizeNullable(requestDto.getEmergencyContact())
        );
        trekker.setTrekGroup(trekGroup);
        trekker.setActive(true);

        return trekker;

    }

    public static void mapToExistingEntity(
            Trekker trekker,
            TrekkerRequestDto requestDto,
            TrekGroup trekGroup
    ) {
        trekker.setFullName(requestDto.getFullName().trim());
        trekker.setPhoneNumber(normalizeNullable(requestDto.getPhoneNumber()));
        trekker.setNationality(normalizeNullable(requestDto.getNationality()));
        trekker.setEmergencyContact(
                normalizeNullable(requestDto.getEmergencyContact())
        );
        trekker.setTrekGroup(trekGroup);
    }

    public static TrekkerResponseDto mapToResponse(Trekker trekker) {
        TrekGroup trekGroup = trekker.getTrekGroup();

        return new TrekkerResponseDto(
                trekker.getId(),
                trekker.getVersion(),
                trekker.getFullName(),
                trekker.getPhoneNumber(),
                trekker.getNationality(),
                trekker.getEmergencyContact(),
                trekker.getActive(),
                trekGroup.getId(),
                trekGroup.getGroupCode(),
                trekGroup.getGroupName(),
                trekker.getCreatedAt(),
                trekker.getModifiedAt()
        );
    }


    private static String normalizeNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
