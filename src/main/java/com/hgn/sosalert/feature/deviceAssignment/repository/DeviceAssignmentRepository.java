package com.hgn.sosalert.feature.deviceAssignment.repository;

import com.hgn.sosalert.feature.deviceAssignment.entity.DeviceAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeviceAssignmentRepository extends JpaRepository<DeviceAssignment, Long> {

    Optional<DeviceAssignment> findByIdAndActiveTrue(Long id);

    Page<DeviceAssignment> findAllByActiveTrue(Pageable pageable);

    Page<DeviceAssignment> findAllByDeviceIdAndActiveTrue(
            Long deviceId,
            Pageable pageable
    );

    Page<DeviceAssignment> findAllByOrderIdAndActiveTrue(
            Long orderId,
            Pageable pageable
    );

    @Query("""
            select assignment
            from DeviceAssignment assignment
            where assignment.device.id = :deviceId
              and assignment.active = true
              and assignment.assignedFrom <= :timestamp
              and (
                    assignment.assignedUntil is null
                    or assignment.assignedUntil >= :timestamp
                  )
            order by assignment.assignedFrom desc
            """)
    List<DeviceAssignment> findMatchingAssignments(
            @Param("deviceId") Long deviceId,
            @Param("timestamp") LocalDateTime timestamp
    );

    @Query("""
            select assignment
            from DeviceAssignment assignment
            where assignment.device.id = :deviceId
              and assignment.active = true
              and (
                    :excludedAssignmentId is null
                    or assignment.id <> :excludedAssignmentId
                  )
              and assignment.assignedFrom <= :effectiveEnd
              and (
                    assignment.assignedUntil is null
                    or assignment.assignedUntil >= :assignedFrom
                  )
            """)
    List<DeviceAssignment> findOverlappingAssignments(
            @Param("deviceId") Long deviceId,
            @Param("assignedFrom") LocalDateTime assignedFrom,
            @Param("effectiveEnd") LocalDateTime effectiveEnd,
            @Param("excludedAssignmentId") Long excludedAssignmentId
    );
}
