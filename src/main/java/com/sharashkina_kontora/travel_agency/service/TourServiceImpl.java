package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.repository.TourRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final LocationService locationService;

    public TourServiceImpl(TourRepository tourRepository, LocationService locationService) {
        this.tourRepository = tourRepository;
        this.locationService = locationService;
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
        Location location = tour.getLocation();
        if(location.getId() == null) {
            throw new RuntimeException("Location without id");
        }
        return tourRepository.save(tour);
    }

    @Override
    @Transactional
    public void delete(Tour tour) {
        Location location = tour.getLocation();

        if(location.getId() == null)
            throw new RuntimeException("Location without id");

        tour.setFlights(new ArrayList<>());
        location.getTours().remove(tour);
        locationService.save(location);
        tourRepository.delete(tour);
    }
}
