package com.hgn.sosalert.feature.alert.repository;

import com.hgn.sosalert.feature.alert.entity.Alert;
import com.hgn.sosalert.feature.alert.enums.AlertStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    Page<Alert> findAllByStatus(
            AlertStatus status,
            Pageable pageable
    );

    Page<Alert> findAllByDeviceId(
            Long deviceId,
            Pageable pageable
    );

    Page<Alert> findAllByOrderId(
            Long orderId,
            Pageable pageable
    );

    Optional<Alert>
    findFirstByDeviceIdAndAlertTimestampBetweenOrderByAlertTimestampDesc(
            Long deviceId,
            LocalDateTime windowStart,
            LocalDateTime windowEnd
    );
}
