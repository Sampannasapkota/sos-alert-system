package com.hgn.sosalert.feature.order.service;

import com.hgn.sosalert.feature.order.enums.OrderStatus;
import com.hgn.sosalert.feature.order.resource.request.OrderRequestDto;
import com.hgn.sosalert.feature.order.resource.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto requestDto);

    OrderResponseDto getOrderById(Long orderId);

    Page<OrderResponseDto> getAllOrders(Pageable pageable);

    Page<OrderResponseDto> getOrdersByTrekGroupId(
            Long trekGroupId,
            Pageable pageable
    );

    Page<OrderResponseDto> getOrdersByStatus(
            OrderStatus status,
            Pageable pageable
    );

    OrderResponseDto updateOrderById(
            Long orderId,
            OrderRequestDto requestDto
    );

    void deactivateOrderById(Long orderId);

}
