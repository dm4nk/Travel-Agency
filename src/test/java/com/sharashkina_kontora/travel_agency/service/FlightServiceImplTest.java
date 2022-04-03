package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Flight;
import com.sharashkina_kontora.travel_agency.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        assertArrayEquals(result.toArray(), flights.toArray());
    }

    @Test
    void findById() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Flight result = flightService.findById(1L).orElse(null);

        assertEquals(result, flight);
    }

    @Test
    void save() {
        when(flightRepository.save(flight)).thenReturn(flight);

        Flight result = flightService.save(flight);

        assertEquals(result, flight);
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void delete() {
        flightService.delete(flight);

        verify(flightRepository, times(1)).delete(flight);
    }
}