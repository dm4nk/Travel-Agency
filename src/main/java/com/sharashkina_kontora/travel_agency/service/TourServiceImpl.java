package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.repository.TourRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    /**
     * Returns all existing tours
     * @return list of tours
     */
    @Override
    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    /**
     * Returns tour by special id
     * @param id
     * @return tour by special id
     */
    @Override
    public Optional<Tour> findById(Long id) {
        return tourRepository.findById(id);
    }

    /**
     * Method to create or update tour or its characteristics
     * @param tour
     * @return tour that was created or changed
     */
    @Override
    @Transactional
    public Tour save(Tour tour) {
        return tourRepository.save(tour);
    }

    /**
     * Method to remove tour from database
     * First, we remove the tour from locations table, then - remove each order, which contains this tour
     * Finally, tour is removed from database
     * @param tour
     */
    @Override
    @Transactional
    public void delete(Tour tour) {
        Location location = tour.getLocation();
        location.getTours().remove(tour);
        locationService.save(location);

        orderService.findAll().stream()
                .filter(order -> tour.getOrders().contains(order))
                .forEach(orderService::delete);

        tourRepository.delete(tour);
    }
}
