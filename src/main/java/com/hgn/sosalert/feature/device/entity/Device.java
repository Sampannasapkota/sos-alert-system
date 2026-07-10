package com.hgn.sosalert.feature.device.entity;

import com.hgn.sosalert.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device extends BaseEntity {

    @Column(name = "device_code", nullable = false, unique = true, length = 100)
    private String deviceCode;

    @Column(name = "display_name", length = 150)
    private String displayName;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;


}
