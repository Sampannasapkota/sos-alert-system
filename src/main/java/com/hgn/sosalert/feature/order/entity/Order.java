package com.hgn.sosalert.feature.order.entity;

import com.hgn.sosalert.common.entity.BaseEntity;
import com.hgn.sosalert.feature.order.enums.OrderStatus;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Column(name = "order_reference", nullable = false, unique = true, length = 150)
    private String orderReference;

    @Column(name = "trek_name", nullable = false, length = 150)
    private String trekName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trek_group_id", nullable = false)
    private TrekGroup trekGroup;

}
