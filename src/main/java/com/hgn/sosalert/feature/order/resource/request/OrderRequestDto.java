package com.hgn.sosalert.feature.order.resource.request;

import com.hgn.sosalert.feature.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank(message = "Order reference is required")
    @Size(max = 50)
    private String orderReference;

    @NotBlank(message = "Trek name is required")
    @Size(max = 150)
    private String trekName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    @NotNull(message = "Trek group ID is required")
    private Long trekGroupId;
}
