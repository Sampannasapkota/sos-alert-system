package com.hgn.sosalert.feature.trekGroup.repository;

import com.hgn.sosalert.feature.trekGroup.entity.TrekGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrekGroupRepository extends JpaRepository<TrekGroup, Long> {
    boolean existsByGroupCode(String groupCode);

    boolean existsByGroupCodeAndIdNot(String groupCode, Long id);

    Optional<TrekGroup> findByIdAndActiveTrue(Long id);

    Page<TrekGroup> findAllByActiveTrue(Pageable pageable);
}
