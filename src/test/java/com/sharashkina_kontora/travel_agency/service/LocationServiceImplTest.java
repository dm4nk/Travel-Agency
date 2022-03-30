package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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
    void findById() {

    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}