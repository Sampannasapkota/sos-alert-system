package com.hgn.sosalert.feature.order.repository;

import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderReference(String orderReference);

    boolean existsByOrderReferenceAndIdNot(
            String orderReference,
            Long id
    );

    Optional<Order> findByIdAndActiveTrue(Long id);

    Page<Order> findAllByActiveTrue(Pageable pageable);

    Page<Order> findAllByTrekGroupIdAndActiveTrue(
            Long trekGroupId,
            Pageable pageable
    );

    Page<Order> findAllByStatusAndActiveTrue(
            OrderStatus status,
            Pageable pageable
    );
}
