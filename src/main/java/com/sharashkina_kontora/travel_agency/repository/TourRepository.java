package com.sharashkina_kontora.travel_agency.repository;

import com.sharashkina_kontora.travel_agency.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findAll(Pageable pageable);

    @Modifying
    @Query("update Tour t set t.freePlaces = :freePlaces where t.id = :id")
    void updateFreePlaces(@Param(value = "id") Long id, @Param(value = "freePlaces") Integer freePlaces);
}
