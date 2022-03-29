package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Flight;
import com.sharashkina_kontora.travel_agency.repository.FlightRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    FlightRepository flightRepository;

    @InjectMocks
    FlightServiceImpl flightService;

    Flight flight;
    LocalDate localDate = LocalDate.now();

    @BeforeEach
    void setUp() {
        flight = Flight.builder()
                .id(1L)
                .departureAirport("d")
                .arrivalAirport("a")
                .date(localDate)
                .build();
    }

    @Test
    void findAll() {
        //объявляем данные
        List<Flight> flights = List.of(flight);
        when(flightRepository.findAll()).thenReturn(flights);

        //идут операции над проверяемым методом
        List<Flight> result = flightService.findAll();

        //проверка
        Assertions.assertArrayEquals(result.toArray(), flights.toArray());
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}