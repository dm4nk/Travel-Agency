package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.repository.LocationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Return all existing locations
     *
     * @return list of locations
     */
    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    /**
     * Method to get location by special id
     *
     * @param id
     * @return location by special id
     */
    @Override
    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    /**
     * Method to create or update location's characteristics
     *
     * @param location
     * @return location that was created or changed
     */
    @Override
    @Transactional
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    /**
     * Method to remove location from database
     * If one of the tours contains this location, it cannot be removed
     *
     * @param location
     */
    @Override
    @Transactional
    public void delete(Location location) {
        locationRepository.delete(location);
    }
}
