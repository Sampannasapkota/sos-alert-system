package com.hgn.sosalert.feature.trekker.entity;

import com.hgn.sosalert.common.entity.BaseEntity;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trekkers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trekker extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "emergency_contact", length = 30)
    private String emergencyContact;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trek_group_id", nullable = false)
    private TrekGroup trekGroup;

}
