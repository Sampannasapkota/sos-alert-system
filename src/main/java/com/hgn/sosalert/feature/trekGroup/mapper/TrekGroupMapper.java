package com.hgn.sosalert.feature.trekGroup.mapper;

import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import com.hgn.sosalert.feature.trekGroup.resource.request.TrekGroupRequestDto;
import com.hgn.sosalert.feature.trekGroup.resource.response.TrekGroupResponseDto;


public class TrekGroupMapper {

    private TrekGroupMapper() {
    }

    public static TrekGroupResponseDto mapToResponse(TrekGroup trekGroup) {
        return new TrekGroupResponseDto(
                trekGroup.getId(),
                trekGroup.getVersion(),
                trekGroup.getGroupCode(),
                trekGroup.getGroupName(),
                trekGroup.getActive(),
                trekGroup.getCreatedAt(),
                trekGroup.getModifiedAt()
        );
    }
    public static TrekGroup mapToEntity(TrekGroupRequestDto trekGroupRequestDto) {
        TrekGroup trekGroup = new TrekGroup();
        trekGroup.setGroupCode(trekGroup.getGroupCode());
        trekGroup.setGroupName(trekGroupRequestDto.getGroupName());
        return trekGroup;
    }
}
