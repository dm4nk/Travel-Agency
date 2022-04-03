package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourServiceImplTest {

    @Mock
    TourRepository tourRepository;

    @Mock
    LocationService locationService;

    @InjectMocks
    TourServiceImpl tourService;

    Tour tour;
    Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id(1L)
                .city("ci")
                .country("co")
                .build();

        tour = Tour.builder()
                .id(2L)
                .freePlaces(3)
                .price(3)
                .duration(3L)
                .location(location)
                .build();
    }

    @Test
    void findAll() {
        List<Tour> tours = List.of(tour);
        when(tourRepository.findAll()).thenReturn(tours);

        List<Tour> result = tourService.findAll();

        assertArrayEquals(result.toArray(), tours.toArray());
    }

    @Test
    void findById() {
        when(tourRepository.findById(2L)).thenReturn(Optional.of(tour));

        Tour result = tourService.findById(2L).orElse(null);

        assertEquals(result, tour);
    }

    @Test
    void save() {
        when(tourRepository.save(tour)).thenReturn(tour);

        Tour result = tourService.save(tour);

        assertEquals(result, tour);
        verify(tourRepository, times(1)).save(tour);
    }

    @Test
    void delete() {
        location.getTours().add(tour);
        when(locationService.save(any())).thenReturn(location);

        tourService.delete(tour);

        assertEquals(0, location.getTours().size());
        verify(locationService, times(1)).save(any());
        verify(tourRepository, times(1)).delete(tour);
    }
}