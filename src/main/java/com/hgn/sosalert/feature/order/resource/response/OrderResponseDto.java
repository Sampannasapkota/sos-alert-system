package com.hgn.sosalert.feature.order.resource.response;

import com.hgn.sosalert.feature.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private Long version;

    private String orderReference;
    private String trekName;
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderStatus status;
    private Boolean active;

    private Long trekGroupId;
    private String trekGroupCode;
    private String trekGroupName;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
