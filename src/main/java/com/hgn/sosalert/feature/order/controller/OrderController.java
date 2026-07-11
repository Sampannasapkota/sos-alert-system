package com.hgn.sosalert.feature.order.controller;

import com.hgn.sosalert.feature.order.enums.OrderStatus;
import com.hgn.sosalert.feature.order.resource.request.OrderRequestDto;
import com.hgn.sosalert.feature.order.resource.response.OrderResponseDto;
import com.hgn.sosalert.feature.order.service.OrderService;
import com.hgn.sosalert.shared.enums.ResponseStatus;
import com.hgn.sosalert.shared.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponseDto<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto requestDto) {

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Order created successfully",
                orderService.createOrder(requestDto)
        );
    }

    @GetMapping
    public ApiResponseDto<Page<OrderResponseDto>> getAllOrders(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Orders fetched successfully",
                orderService.getAllOrders(pageable)
        );
    }

    @GetMapping("/{id}")
    public ApiResponseDto<OrderResponseDto> getOrderById(
            @PathVariable Long id) {

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Order fetched successfully",
                orderService.getOrderById(id)
        );
    }

    @GetMapping("/trek-group/{trekGroupId}")
    public ApiResponseDto<Page<OrderResponseDto>> getOrdersByTrekGroupId(
            @PathVariable Long trekGroupId,
            @ParameterObject Pageable pageable) {

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Orders fetched successfully",
                orderService.getOrdersByTrekGroupId(
                        trekGroupId,
                        pageable
                )
        );
    }

    @GetMapping("/status/{status}")
    public ApiResponseDto<Page<OrderResponseDto>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @ParameterObject Pageable pageable) {

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Orders fetched successfully",
                orderService.getOrdersByStatus(status, pageable)
        );
    }

    @PutMapping("/{id}")
    public ApiResponseDto<OrderResponseDto> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDto requestDto) {

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Order updated successfully",
                orderService.updateOrderById(id, requestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deactivateOrder(
            @PathVariable Long id) {

        orderService.deactivateOrderById(id);

        return new ApiResponseDto<>(
                ResponseStatus.SUCCESS.name(),
                "Order deactivated successfully"
        );
    }
}
