package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TourService extends BasicService<Tour> {
    List<Tour> findMostPopular();

    List<Tour> findLessPopular();

    List<Tour> findCheapest();

    Page<Tour> findAll(Pageable pageable);
}
