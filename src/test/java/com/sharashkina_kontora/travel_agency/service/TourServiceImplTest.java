package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Order;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourServiceImplTest {

    @Mock
    OrderService orderService;

    @Mock
    TourRepository tourRepository;

    @Mock
    LocationService locationService;

    @InjectMocks
    TourServiceImpl tourService;

    Tour tour;
    Location location;
    Order order;

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

        order = Order.builder()
                .id(3L)
                .tour(tour)
                .build();

        tour.setOrders(Set.of(order));
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
        tourService.delete(tour);

        verify(tourRepository, times(1)).delete(tour);
    }
}