package com.hgn.sosalert.feature.order.mapper;

import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.order.resource.request.OrderRequestDto;
import com.hgn.sosalert.feature.order.resource.response.OrderResponseDto;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static Order mapToEntity(OrderRequestDto orderRequestDto, TrekGroup trekGroup) {
        Order order = new Order();
        order.setOrderReference(orderRequestDto.getOrderReference());
        order.setTrekName(orderRequestDto.getTrekName());
        order.setStartDate(orderRequestDto.getStartDate());
        order.setEndDate(orderRequestDto.getEndDate());
        order.setStatus(orderRequestDto.getStatus());
        order.setActive(true);
        order.setTrekGroup(trekGroup);

        return order;
    }

    public static void mapToExistingEntity(Order order, OrderRequestDto orderRequestDto, TrekGroup trekGroup) {
        order.setOrderReference(orderRequestDto.getOrderReference());
        order.setTrekName(orderRequestDto.getTrekName());
        order.setStartDate(orderRequestDto.getStartDate());
        order.setEndDate(orderRequestDto.getEndDate());
        order.setStatus(orderRequestDto.getStatus());
        order.setTrekGroup(trekGroup);
    }

    public static OrderResponseDto mapToResponse(Order order) {
        TrekGroup trekGroup = order.getTrekGroup();
        return new OrderResponseDto(
                order.getId(),
                order.getVersion(),
                order.getOrderReference(),
                order.getTrekName(),
                order.getStartDate(),
                order.getEndDate(),
                order.getStatus(),
                order.getActive(),
                trekGroup.getId(),
                trekGroup.getGroupCode(),
                trekGroup.getGroupName(),
                order.getCreatedAt(),
                order.getModifiedAt()
        );

    }
}
