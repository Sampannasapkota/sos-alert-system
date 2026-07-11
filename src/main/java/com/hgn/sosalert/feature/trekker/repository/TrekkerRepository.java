package com.hgn.sosalert.feature.trekker.repository;

import com.hgn.sosalert.feature.trekker.entity.Trekker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrekkerRepository extends JpaRepository<Trekker, Long> {

    Optional<Trekker> findByIdAndActiveTrue(Long id);

    Page<Trekker> findAllByActiveTrue(Pageable pageable);

    Page<Trekker> findAllByTrekGroupIdAndActiveTrue(
            Long trekGroupId,
            Pageable pageable
    );

    boolean existsByTrekGroupIdAndActiveTrue(Long trekGroupId);
}
