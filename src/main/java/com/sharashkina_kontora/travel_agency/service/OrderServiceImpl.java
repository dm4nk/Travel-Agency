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

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Returns all existing orders
     *
     * @return list of orders
     */
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * Returns order by special id
     *
     * @param id
     * @return order by special id
     */
    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * Method to create or update order or its characteristics
     *
     * @param order
     * @return order that was created or changed
     */
    @Override
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    /**
     * Method to remove order from database
     * First, we remove the order from users table, then - remove the order from tours table
     * Finally, order is removed from database
     *
     * @param order
     */
    @Override
    @Transactional
    public void delete(Order order) {
        User user = order.getUser();
        user.getOrders().remove(order);

        Tour tour = order.getTour();
        tour.getOrders().remove(order);

        orderRepository.delete(order);
    }
}
