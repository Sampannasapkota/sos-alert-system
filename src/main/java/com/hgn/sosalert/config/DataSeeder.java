package com.hgn.sosalert.config;

import com.hgn.sosalert.feature.device.entity.Device;
import com.hgn.sosalert.feature.device.repository.DeviceRepository;
import com.hgn.sosalert.feature.deviceAssignment.entity.DeviceAssignment;
import com.hgn.sosalert.feature.deviceAssignment.repository.DeviceAssignmentRepository;
import com.hgn.sosalert.feature.order.entity.Order;
import com.hgn.sosalert.feature.order.enums.OrderStatus;
import com.hgn.sosalert.feature.order.repository.OrderRepository;
import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import com.hgn.sosalert.feature.trekGroup.repository.TrekGroupRepository;
import com.hgn.sosalert.feature.trekker.entity.Trekker;
import com.hgn.sosalert.feature.trekker.repository.TrekkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        name = "app.seed.enabled",
        havingValue = "true"
)
public class DataSeeder {

    private final DeviceRepository deviceRepository;
    private final TrekGroupRepository trekGroupRepository;
    private final OrderRepository orderRepository;
    private final TrekkerRepository trekkerRepository;
    private final DeviceAssignmentRepository assignmentRepository;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> seedData();

    }

    @Transactional
    public void seedData() {
        if (dataAlreadyExists()) {
            log.info("Seed data already exists. Skipping database seeding.");
            return;
        }

        log.info("Starting database seeding.");

        TrekGroup everestGroup = createTrekGroup(
                "EVEREST-DEMO-01",
                "Everest Demo Group"
        );

        TrekGroup annapurnaGroup = createTrekGroup(
                "ANNAPURNA-DEMO-01",
                "Annapurna Demo Group"
        );

        trekGroupRepository.saveAll(
                List.of(everestGroup, annapurnaGroup)
        );

        Trekker trekker1 = createTrekker(
                "Xia Houng",
                "9800000001",
                "Chinese",
                "9810000001",
                everestGroup
        );

        Trekker trekker2 = createTrekker(
                "Alex Costa",
                "9800000002",
                "Spanish",
                "9810000002",
                everestGroup
        );

        Trekker trekker3 = createTrekker(
                "Sampanna Sapkota",
                "9800000003",
                "Nepali",
                "9810000003",
                annapurnaGroup
        );

        trekkerRepository.saveAll(
                List.of(trekker1, trekker2, trekker3)
        );

        Device everestDevice = createDevice(
                "GPS-DEMO-001",
                "Everest Shared GPS"
        );

        Device annapurnaDevice = createDevice(
                "GPS-DEMO-002",
                "Annapurna Shared GPS"
        );

        Device spareDevice = createDevice(
                "GPS-DEMO-003",
                "Emergency Spare GPS"
        );

        deviceRepository.saveAll(
                List.of(
                        everestDevice,
                        annapurnaDevice,
                        spareDevice
                )
        );

        LocalDate today = LocalDate.now();

        Order everestOrder = createOrder(
                "ORDER-EBC-DEMO-001",
                "Everest Base Camp Trek",
                today.minusDays(1),
                today.plusDays(10),
                everestGroup
        );

        Order annapurnaOrder = createOrder(
                "ORDER-ABC-DEMO-001",
                "Annapurna Base Camp Trek",
                today.minusDays(1),
                today.plusDays(8),
                annapurnaGroup
        );

        orderRepository.saveAll(
                List.of(everestOrder, annapurnaOrder)
        );

        DeviceAssignment everestAssignment = createAssignment(
                everestDevice,
                everestOrder,
                today.minusDays(1).atStartOfDay(),
                today.plusDays(10).atTime(LocalTime.MAX)
        );

        DeviceAssignment annapurnaAssignment = createAssignment(
                annapurnaDevice,
                annapurnaOrder,
                today.minusDays(1).atStartOfDay(),
                today.plusDays(8).atTime(LocalTime.MAX)
        );

        assignmentRepository.saveAll(
                List.of(
                        everestAssignment,
                        annapurnaAssignment
                )
        );

        log.info(
                "Database seeding completed. groups={}, trekkers={}, devices={}, orders={}, assignments={}",
                2,
                3,
                3,
                2,
                2
        );
    }

    private boolean dataAlreadyExists() {
        return trekGroupRepository.existsByGroupCode(
                "EVEREST-DEMO-01"
        );
    }

    private TrekGroup createTrekGroup(
            String groupCode,
            String groupName
    ) {
        return TrekGroup.builder()
                .groupCode(groupCode)
                .groupName(groupName)
                .active(true)
                .build();
    }

    private Trekker createTrekker(
            String fullName,
            String phoneNumber,
            String nationality,
            String emergencyContact,
            TrekGroup trekGroup
    ) {
        return Trekker.builder()
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .nationality(nationality)
                .emergencyContact(emergencyContact)
                .trekGroup(trekGroup)
                .active(true)
                .build();
    }

    private Device createDevice(
            String deviceCode,
            String displayName
    ) {
        return Device.builder()
                .deviceCode(deviceCode)
                .displayName(displayName)
                .active(true)
                .build();
    }

    private Order createOrder(
            String orderReference,
            String trekName,
            LocalDate startDate,
            LocalDate endDate,
            TrekGroup trekGroup
    ) {
        return Order.builder()
                .orderReference(orderReference)
                .trekName(trekName)
                .startDate(startDate)
                .endDate(endDate)
                .status(OrderStatus.ACTIVE)
                .trekGroup(trekGroup)
                .active(true)
                .build();
    }

    private DeviceAssignment createAssignment(
            Device device,
            Order order,
            LocalDateTime assignedFrom,
            LocalDateTime assignedUntil
    ) {
        return DeviceAssignment.builder()
                .device(device)
                .order(order)
                .assignedFrom(assignedFrom)
                .assignedUntil(assignedUntil)
                .active(true)
                .build();

    }
}
