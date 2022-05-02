package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.repository.TourRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    public TourServiceImpl(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    /**
     * Returns all existing tours
     *
     * @return list of tours
     */
    @Override
    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    /**
     * Returns tour by special id
     *
     * @param id
     * @return tour by special id
     */
    @Override
    public Optional<Tour> findById(Long id) {
        return tourRepository.findById(id);
    }

    @Override
    public List<Tour> findMostPopular() {
        return findAll().stream().sorted(Comparator.comparingInt(o -> o.getOrders().size()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tour> findLessPopular() {
        List<Tour> mostPopular = findMostPopular();
        Collections.reverse(mostPopular);
        return mostPopular;
    }

    @Override
    public List<Tour> findCheapest() {
        return findAll().stream()
                .sorted(Comparator.comparingDouble(o -> o.getPrice() / (double) o.getDuration()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateFreePlaces(Tour tour, Integer freePlaces) {
        tourRepository.updateFreePlaces(tour.getId(), freePlaces);
    }

    @Override
    public Page<Tour> findAll(Pageable pageable) {
        return tourRepository.findAll(pageable);
    }

    /**
     * Method to create or update tour or its characteristics
     *
     * @param tour
     * @return tour that was created or changed
     */
    @Override
    @Transactional
    public Tour save(Tour tour) {
        Location location = tour.getLocation();
        location.getTours().add(tour);

        return tourRepository.save(tour);
    }

    /**
     * Method to remove tour from database
     * First, we remove the tour from locations table, then - remove each order, which contains this tour
     * Finally, tour is removed from database
     *
     * @param tour
     */
    @Override
    @Transactional
    public void delete(Tour tour) {
        Location location = tour.getLocation();
        location.getTours().remove(tour);

        tourRepository.delete(tour);
    }
}
