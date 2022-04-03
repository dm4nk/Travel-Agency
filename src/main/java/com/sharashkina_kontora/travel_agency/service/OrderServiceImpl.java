package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Order;
import com.sharashkina_kontora.travel_agency.domain.Tour;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final TourService tourService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, TourService tourService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.tourService = tourService;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void delete(Order order) {
        User user = order.getUser();
        user.getOrders().remove(order);
        userService.save(user);

        Tour tour = order.getTour();
        tour.getOrders().remove(order);
        tourService.save(tour);

        orderRepository.delete(order);
    }
}
