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

        locationService.save(location);

        Tour tour1 = Tour.builder()
                .freePlaces(1)
                .price(1)
                .duration(1L)
                .location(location)
                .build();

        Tour tour2 = Tour.builder()
                .freePlaces(2)
                .price(2)
                .duration(2L)
                .location(location)
                .build();

        Tour saved1 = tourService.save(tour1);
        Tour saved2 = tourService.save(tour2);

        saved1.setFreePlaces(6);
        saved1.setFreePlaces(7);
        saved1.setFreePlaces(8);
        tourService.save(saved1);
        tourService.delete(saved2);
    }
}
