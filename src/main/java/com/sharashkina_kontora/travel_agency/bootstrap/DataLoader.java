package com.sharashkina_kontora.travel_agency.bootstrap;

import com.sharashkina_kontora.travel_agency.domain.*;
import com.sharashkina_kontora.travel_agency.service.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final TourService tourService;
    private final LocationService locationService;
    private final FlightService flightService;
    private final RoleService roleService;
    private final UserService userService;
    private final OrderService orderService;

    public DataLoader(TourService tourService, LocationService locationService, FlightService flightService, RoleService roleService, UserService userService, OrderService orderService) {
        this.tourService = tourService;
        this.locationService = locationService;
        this.flightService = flightService;
        this.roleService = roleService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Location location = Location.builder()
                .city("ci")
                .country("co")
                .build();

        locationService.save(location);

        Flight flight = Flight.builder()
                .name("floig")
                .departureAirport("da")
                .arrivalAirport("ar")
                .date(LocalDate.now())
                .build();

        Flight flight2 = Flight.builder()
                .name("floig2")
                .departureAirport("da")
                .arrivalAirport("ar")
                .date(LocalDate.now())
                .build();

        Flight flight3 = Flight.builder()
                .name("floig3")
                .departureAirport("da")
                .arrivalAirport("ar")
                .date(LocalDate.now())
                .build();

        Flight savedFlight = flightService.save(flight);
        Flight savedFlight2 = flightService.save(flight2);
        Flight savedFlight3 = flightService.save(flight3);

        Tour tour1 = Tour.builder()
                .freePlaces(1)
                .price(1)
                .duration(1L)
                .location(location)
                .build();

        //tour1.setFlights(Set.of(savedFlight));

        tourService.save(tour1);

        tour1.setFlights(Set.of(savedFlight, savedFlight2, savedFlight3));

        tourService.save(tour1);

        Role role = Role.builder()
                .name("adm")
                .build();

        roleService.save(role);

        User user = User.builder()
                .firstName("Lilya")
                .password("123")
                .role(role)
                .build();

        userService.save(user);

        Order order1 = Order.builder()
                .user(user)
                .tour(tour1)
                .status(Status.PLANNED)
                .build();

        Order order2 = Order.builder()
                .user(user)
                .tour(tour1)
                .status(Status.DONE)
                .build();

        orderService.save(order1);
        orderService.save(order2);

        tourService.delete(tourService.findById(1L).get());
    }
}
