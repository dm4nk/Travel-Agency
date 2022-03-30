package com.sharashkina_kontora.travel_agency.bootstrap;

import com.sharashkina_kontora.travel_agency.domain.Location;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.service.LocationService;
import com.sharashkina_kontora.travel_agency.service.TourService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final TourService tourService;
    private final LocationService locationService;

    public DataLoader(TourService tourService, LocationService locationService) {
        this.tourService = tourService;
        this.locationService = locationService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Location location = Location.builder()
                .city("ci")
                .country("co")
                .build();

        Location savedLocation = locationService.save(location);

        Tour tour1 = Tour.builder()
                .freePlaces(1L)
                .price(1L)
                .duration(1L)
                .location(savedLocation)
                .build();

        Tour tour2 = Tour.builder()
                .freePlaces(2L)
                .price(2L)
                .duration(2L)
                .location(savedLocation)
                .build();

        Tour saved1 = tourService.save(tour1);
        Tour saved2 = tourService.save(tour2);

        tourService.delete(saved1);
    }
}
