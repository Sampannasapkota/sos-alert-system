package com.hgn.sosalert.feature.alert.entity;

import com.hgn.sosalert.common.entity.BaseEntity;
import com.hgn.sosalert.feature.alert.enums.AlertStatus;
import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trek_group_id")
    private TrekGroup trekGroup;

    @Column(
            name = "latitude",
            nullable = false,
            precision = 10,
            scale = 7
    )
    private BigDecimal latitude;

    @Column(
            name = "longitude",
            nullable = false,
            precision = 10,
            scale = 7
    )
    private BigDecimal longitude;

    //Date and time reported by the GPS device
    @Column(name = "alert_timestamp", nullable = false)
    private LocalDateTime alertTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private AlertStatus status = AlertStatus.RECEIVED;

    @Column(name = "claimed_by")
    private String claimedBy;

    @Column(name = "claimed_at")
    private LocalDateTime claimedAt;

    @Column(name = "escalated_at")
    private LocalDateTime escalatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
