package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Status;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.exceptions.NoFreePlacesException;
import com.sharashkina_kontora.travel_agency.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    UserService userService;

    @Mock
    TourService tourService;

    @Mock
    OrderRepository orderRepository;

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
    void save() throws Exception {
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertEquals(result, order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void delete() throws Exception {
        orderService.delete(order);

        verify(orderRepository, times(1)).delete(order);
    }
}