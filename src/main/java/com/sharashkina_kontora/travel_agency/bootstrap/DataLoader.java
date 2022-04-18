package com.sharashkina_kontora.travel_agency.bootstrap;

import com.sharashkina_kontora.travel_agency.domain.*;
import com.sharashkina_kontora.travel_agency.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@Slf4j
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
                .city("Moscow")
                .country("Russia")
                .build();

        Location location2 = Location.builder()
                .city("Saint-Petersberg")
                .country("Russia")
                .build();
        locationService.save(location);
        locationService.save(location2);

        Flight flight = Flight.builder()
                .name("flight1")
                .departureAirport("Kurumoch")
                .arrivalAirport("Domodedovo")
                .date(LocalDate.now())
                .build();

        Flight flight2 = Flight.builder()
                .name("flight3")
                .departureAirport("Domodedovo")
                .arrivalAirport("Pulkovo")
                .date(LocalDate.now())
                .build();

        Flight flight3 = Flight.builder()
                .name("flight2")
                .departureAirport("Pulkovo")
                .arrivalAirport("Kurumoch")
                .date(LocalDate.now())
                .build();

        Flight flight4 = Flight.builder()
                .name("flight4")
                .departureAirport("Pulkovo")
                .arrivalAirport("Kurumoch")
                .date(LocalDate.now())
                .build();

        Flight savedFlight = flightService.save(flight);
        Flight savedFlight2 = flightService.save(flight2);
        Flight savedFlight3 = flightService.save(flight3);
        Flight savedFlight4 = flightService.save(flight4);

        Tour tour1 = Tour.builder()
                .name("NICE TOUR")
                .freePlaces(1)
                .price(1)
                .duration(1L)
                .location(location)
                .build();

        //tour1.setFlights(Set.of(savedFlight));

        tourService.save(tour1);

        tour1.setFlights(Set.of(savedFlight, savedFlight2, savedFlight3));

        tourService.save(tour1);

        Tour tour2 = Tour.builder()
                .name("NICEEEEE TOUR 2")
                .freePlaces(3)
                .price(1)
                .duration(1L)
                .location(location)
                .build();

        tourService.save(tour2);
        tour2.setFlights(Set.of(savedFlight4));
        tourService.save(tour2);

        Role role = Role.builder()
                .name("adm")
                .build();

        roleService.save(role);

        Role role2 = Role.builder()
                .name("user")
                .build();

        roleService.save(role2);

        User user = User.builder()
                .firstName("Lilya")
                .password("123")
                .role(role)
                .build();

        userService.save(user);

        User user2 = User.builder()
                .firstName("Tourist")
                .password("123")
                .role(role2)
                .build();

        userService.save(user2);

        User user3 = User.builder()
                .firstName("Dima")
                .email("dim@gmaill.com")
                .password("123")
                .role(role)
                .build();

        userService.save(user3);

        Order order1 = Order.builder()
                .user(user3)
                .tour(tour1)
                .status(Status.PLANNED)
                .build();

        Order order2 = Order.builder()
                .user(user3)
                .tour(tour1)
                .status(Status.PLANNED)
                .build();

        Order order3 = Order.builder()
                .user(user3)
                .tour(tour2)
                .status(Status.PLANNED)
                .build();

        orderService.save(order1);
        orderService.save(order2);
        orderService.save(order3);

        //orderService.delete(order1);

        //userService.delete(userService.findById(1L).get());
    }
}
