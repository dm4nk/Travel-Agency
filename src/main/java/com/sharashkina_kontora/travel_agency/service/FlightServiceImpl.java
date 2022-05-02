package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Flight;
import com.sharashkina_kontora.travel_agency.exceptions.FlightInUseException;
import com.sharashkina_kontora.travel_agency.repository.FlightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Returns all existing flights
     *
     * @return list of flights
     */
    @Override
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    /**
     * Returns flight by special id
     *
     * @param id
     * @return flight by special id
     */
    @Override
    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    /**
     * Method to create or update flight's characteristics
     *
     * @param flight
     * @return flight, which was created or changed
     */
    @Override
    @Transactional
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    /**
     * Method to remove flight from database
     * If one of the tours contains this flight, it cannot be removed
     *
     * @param flight
     */
    @Override
    @Transactional
    public void delete(Flight flight) throws Exception {
        if (!flight.getTours().isEmpty()) throw new FlightInUseException("Flight in use");
        flightRepository.delete(flight);
    }

    @Override
    public Page<Flight> findAll(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }
}
