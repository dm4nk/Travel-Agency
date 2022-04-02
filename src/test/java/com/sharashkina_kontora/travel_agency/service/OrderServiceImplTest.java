package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.*;
import com.sharashkina_kontora.travel_agency.repository.OrderRepository;
import com.sharashkina_kontora.travel_agency.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserServiceImpl userService;

    @Mock
    TourServiceImpl tourService;

    @InjectMocks
    OrderServiceImpl orderService;

    User user;
    Order order;
    Tour tour;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(4L)
                .firstName("Dima")
                .lastName("Prokopovich")
                .email("dim.xx2011@yandex.ru")
                .phoneNumber("89277583192")
                .birthday(LocalDate.now())
                .password("123456")
                .build();

        tour = Tour.builder()
                .id(2L)
                .freePlaces(3)
                .price(3)
                .duration(3L)
                .build();

        order = Order.builder()
                .id(1L)
                .user(user)
                .tour(tour)
                .status(Status.PLANNED)
                .build();
    }

    @Test
    void findAll() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        assertArrayEquals(result.toArray(), orders.toArray());
    }

    @Test
    void findById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L).orElse(null);

        assertEquals(result, order);
    }

    @Test
    void save() {
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.findById(1L).orElse(null);

        assertEquals(result, order);
        assertEquals(1, user.getOrders().size());
        assertEquals(1, tour.getOrders().size());
        verify(tourService, times(1)).save(tour);
        verify(userService, times(1)).save(user);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void delete() {
        tour.getOrders().add(order);
        user.getOrders().add(order);
        when(tourService.save(any())).thenReturn(tour);
        when(userService.save(any())).thenReturn(user);

        orderService.delete(order);

        assertEquals(0, tour.getOrders().size());
        assertEquals(0, user.getOrders().size());
        verify(tourService, times(1)).save(any());
        verify(userService, times(1)).save(any());
        verify(orderRepository, times(2)).delete(order);
    }
}