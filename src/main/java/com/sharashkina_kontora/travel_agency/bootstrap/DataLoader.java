package com.sharashkina_kontora.travel_agency.bootstrap;

import com.sharashkina_kontora.travel_agency.domain.*;
import com.sharashkina_kontora.travel_agency.service.*;
import com.sharashkina_kontora.travel_agency.view.Constants;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Location location = Location.builder()
                .city("Moscow")
                .country("Russia")
                .build();

        Location location2 = Location.builder()
                .city("Grido Islando")
                .country("XXX")
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
                .departureAirport("Karaganda")
                .arrivalAirport("Krojopol'")
                .date(LocalDate.now())
                .build();

        Flight savedFlight = flightService.save(flight);
        Flight savedFlight2 = flightService.save(flight2);
        Flight savedFlight3 = flightService.save(flight3);
        Flight savedFlight4 = flightService.save(flight4);

        Tour tour1 = Tour.builder()
                .name("Excursion to England bars")
                .freePlaces(300)
                .price(90)
                .duration(14L)
                .location(location)
                .build();

        tourService.save(tour1);

        tour1.setFlights(Set.of(savedFlight, savedFlight2, savedFlight3));

        tourService.save(tour1);

        Tour tour2 = Tour.builder()
                .name("Grido Islando")
                .freePlaces(3)
                .price(300)
                .duration(90L)
                .location(location2)
                .build();

        tourService.save(tour2);
        tour2.setFlights(Set.of(savedFlight4));
        tourService.save(tour2);

        Role role = Role.builder()
                .name(Constants.ADMIN)
                .build();

        roleService.save(role);

        Role role2 = Role.builder()
                .name("user")
                .build();

        roleService.save(role2);

        User user = User.builder()
                .firstName("Vitya")
                .lastName("Mineev")
                .email("user@gmail.com")
                .password("123")
                .role(role2)
                .build();

        userService.save(user);

        User user3 = User.builder()
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@gmail.com")
                .password("123")
                .role(role)
                .build();

        userService.save(user3);

        Order order1 = Order.builder()
                .user(user)
                .tour(tour1)
                .status(Status.PLANNED)
                .build();

        Order order2 = Order.builder()
                .user(user)
                .tour(tour1)
                .status(Status.PLANNED)
                .build();

        Order order3 = Order.builder()
                .user(user)
                .tour(tour2)
                .status(Status.PLANNED)
                .build();

        orderService.save(order1);
        orderService.save(order2);
        orderService.save(order3);
    }
}
