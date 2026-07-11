package com.hgn.sosalert.feature.deviceAssignment.entity;

import com.hgn.sosalert.common.entity.BaseEntity;
import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_assignments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "assigned_from", nullable = false)
    private LocalDateTime assignedFrom;

    @Column(name = "assigned_until")
    private LocalDateTime assignedUntil;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

}
