package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {
    @Mock
    LocationRepository locationRepository;

    @InjectMocks
    LocationServiceImpl locationService;

    Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .id(1L)
                .city("ci")
                .country("co")
                .build();
    }

    @Test
    void findAll() {
        List<Location> locations = List.of(location);
        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.findAll();

        assertArrayEquals(result.toArray(), locations.toArray());
    }

    @Test
    void findById() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Location result = locationService.findById(1L).orElse(null);

        assertEquals(result, location);
    }

    @Test
    void save() {
        when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.save(location);

        assertEquals(result, location);
        verify(locationRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        locationService.delete(location);

        verify(locationRepository, times(1)).delete(location);
    }
}