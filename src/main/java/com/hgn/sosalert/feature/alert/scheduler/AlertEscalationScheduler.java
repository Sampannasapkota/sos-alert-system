package com.hgn.sosalert.feature.alert.scheduler;

import com.hgn.sosalert.feature.alert.entity.Alert;
import com.hgn.sosalert.feature.alert.enums.AlertStatus;
import com.hgn.sosalert.feature.alert.repository.AlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertEscalationScheduler {

    private static final long ESCALATION_MINUTES = 5;

    private final AlertRepository alertRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void escalateAlerts() {

        LocalDateTime threshold =
                LocalDateTime.now()
                        .minusMinutes(ESCALATION_MINUTES);

        List<Alert> alerts =
                alertRepository.findAlertsForEscalation(
                        AlertStatus.RECEIVED,
                        threshold
                );

        if (alerts.isEmpty()) {
            return;
        }

        for (Alert alert : alerts) {

            alert.setStatus(AlertStatus.ESCALATED);
            alert.setEscalatedAt(LocalDateTime.now());

            log.warn(
                    "Alert escalated. alertId={}",
                    alert.getId()
            );
        }

        log.info(
                "{} alerts escalated.",
                alerts.size()
        );
    }
}
