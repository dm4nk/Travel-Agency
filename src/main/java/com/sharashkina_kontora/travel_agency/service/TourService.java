package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Tour;

import java.util.List;

public interface TourService extends BasicService<Tour> {
    List<Tour> findMostPopular();

    List<Tour> findLessPopular();

    List<Tour> findCheapest();
}
