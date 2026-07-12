package com.hgn.sosalert.feature.alert.repository;

import com.hgn.sosalert.feature.alert.entity.Alert;
import com.hgn.sosalert.feature.alert.enums.AlertStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select alert
            from Alert alert
            where alert.id = :alertId
            """)
    Optional<Alert> findByIdForUpdate(
            @Param("alertId") Long alertId
    );
}
