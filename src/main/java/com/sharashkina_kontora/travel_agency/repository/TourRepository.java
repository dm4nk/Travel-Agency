package com.sharashkina_kontora.travel_agency.repository;

import com.sharashkina_kontora.travel_agency.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query(value = "select count(*) from Tours t inner join Orders o", nativeQuery = true)
    Integer findMostPopular();
}
