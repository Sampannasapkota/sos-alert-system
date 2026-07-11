package com.hgn.sosalert.feature.order.service.impl;

import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.order.enums.OrderStatus;
import com.hgn.sosalert.feature.order.exception.OrderAlreadyExistsException;
import com.hgn.sosalert.feature.order.exception.OrderNotFoundException;
import com.hgn.sosalert.feature.order.mapper.OrderMapper;
import com.hgn.sosalert.feature.order.repository.OrderRepository;
import com.hgn.sosalert.feature.order.resource.request.OrderRequestDto;
import com.hgn.sosalert.feature.order.resource.response.OrderResponseDto;
import com.hgn.sosalert.feature.order.service.OrderService;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import com.hgn.sosalert.feature.trekGroup.exception.TrekGroupNotFoundException;
import com.hgn.sosalert.feature.trekGroup.repository.TrekGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final TrekGroupRepository trekGroupRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        validateDates(requestDto);
        String reference = requestDto.getOrderReference().trim().toUpperCase();

        if (orderRepository.existsByOrderReference(reference)) {
            log.error("Order reference already exists.");
            throw new OrderAlreadyExistsException("Order reference already exists.");
        }

        TrekGroup trekGroup = findActiveTrekGroupById(requestDto.getTrekGroupId());

        Order order = OrderMapper.mapToEntity(requestDto, trekGroup);
        order.setOrderReference(reference);
        order.setActive(true);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {
        return OrderMapper.mapToResponse(findActiveOrderById(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAllByActiveTrue(pageable)
                .map(OrderMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersByTrekGroupId(Long trekGroupId, Pageable pageable) {
        findActiveTrekGroupById(trekGroupId);

        return orderRepository.findAllByTrekGroupIdAndActiveTrue(trekGroupId, pageable)
                .map(OrderMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {

        return orderRepository.findAllByStatusAndActiveTrue(status, pageable)
                .map(OrderMapper::mapToResponse);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderById(Long orderId, OrderRequestDto requestDto) {
        validateDates(requestDto);
        Order order = findActiveOrderById(orderId);

        String reference = requestDto.getOrderReference().trim().toUpperCase();

        if (orderRepository.existsByOrderReferenceAndIdNot(reference, orderId)) {
            throw new OrderAlreadyExistsException("Order reference already exists.");
        }
        TrekGroup trekGroup = findActiveTrekGroupById(requestDto.getTrekGroupId());

        OrderMapper.mapToExistingEntity(order, requestDto, trekGroup);

        order.setOrderReference(reference);

        return OrderMapper.mapToResponse(order);
    }

    @Override
    @Transactional
    public void deactivateOrderById(Long orderId) {

        Order order = findActiveOrderById(orderId);

        order.setActive(false);

        log.info("Order deactivated successfully. id={}", orderId);

    }

    private void validateDates(OrderRequestDto requestDto) {

        if (requestDto.getEndDate().isBefore(requestDto.getStartDate())) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }
    }

    private Order findActiveOrderById(Long orderId) {

        return orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> {

                    log.error("Order not found. id={}", orderId);

                    return new OrderNotFoundException("Order not found.");
                });
    }

    private TrekGroup findActiveTrekGroupById(Long trekGroupId) {

        return trekGroupRepository.findByIdAndActiveTrue(trekGroupId)
                .orElseThrow(() -> {

                    log.error("Trek group not found. id={}", trekGroupId);

                    return new TrekGroupNotFoundException("Trek group not found.");
                });
    }
}
