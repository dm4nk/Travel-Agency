package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Flight;
import com.sharashkina_kontora.travel_agency.repository.FlightRepository;
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

    @Override
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    @Override
    @Transactional
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    @Transactional
    public void delete(Flight flight) {
        flightRepository.delete(flight);
    }
}
