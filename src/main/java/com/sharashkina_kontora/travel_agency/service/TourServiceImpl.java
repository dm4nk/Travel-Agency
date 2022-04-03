package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.repository.TourRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final LocationService locationService;
    private final OrderService orderService;

    public TourServiceImpl(TourRepository tourRepository, LocationService locationService, @Lazy OrderService orderService) {
        this.tourRepository = tourRepository;
        this.locationService = locationService;
        this.orderService = orderService;
    }

    @Override
    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    @Override
    public Optional<Tour> findById(Long id) {
        return tourRepository.findById(id);
    }

    @Override
    @Transactional
    public Tour save(Tour tour) {
        return tourRepository.save(tour);
    }

    @Override
    @Transactional
    public void delete(Tour tour) {
        Location location = tour.getLocation();
        location.getTours().remove(tour);
        locationService.save(location);

        orderService.findAll().stream().filter(order -> tour.getOrders().contains(order))
                .forEach(orderService::delete);

        tourRepository.delete(tour);
    }
}
