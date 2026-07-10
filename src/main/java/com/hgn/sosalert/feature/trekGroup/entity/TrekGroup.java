package com.hgn.sosalert.feature.trekGroup.entity;

import com.hgn.sosalert.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "trek_groups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrekGroup extends BaseEntity {

    @Column(name = "group_code", nullable = false, unique = true, length = 50)
    private String groupCode;

    @Column(name = "group_name", nullable = false, length = 150)
    private String groupName;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

}
