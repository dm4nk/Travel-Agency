package com.sharashkina_kontora.travel_agency.repository;

import com.sharashkina_kontora.travel_agency.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findAll(Pageable pageable);
}
