package com.inditex.zboost.service;

import com.inditex.zboost.entity.Order;
import com.inditex.zboost.entity.OrderDetail;
import com.inditex.zboost.entity.ProductOrderItem;
import com.inditex.zboost.exception.NotFoundException;
import com.inditex.zboost.repository.OrderRepository;
import com.inditex.zboost.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Order> findOrders(int limit) {
        return orderRepository.getOrders(limit);
    }

    @Override
    public List<Order> findOrdersBetweenDates(Date fromDate, Date toDate) {
        return this.orderRepository.getOrdersBetweenDates(fromDate, toDate);
    }

    @Override
    public OrderDetail findOrderDetail(long orderId) {

        /**
         * TODO: EXERCISE 2.b) Retrieve the details of an order given its ID
         *
         * Check {@link orderRepository#getOrderDetail(orderId)} and
         * {@link productRepository#getProductOrderItemsFromOrder(orderId)} methods.
         *
         * TODO: EXERCISE 2.c) Add cache to the findOrderDetail method.
         *
         * Add a spring cache of type "simple". To do so, follow the spring boot cache
         * documentation:
         * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.caching
         */

        // Invoke the repository to retrieve the details of an order.
        OrderDetail orderDetail = orderRepository.getOrderDetail(orderId);

        // Once you have retrieved the order details, you would need to retrieve the
        // products that are part of it...
        List<ProductOrderItem> products = productRepository.getProductOrderItemsFromOrder(orderId);

        orderDetail.setProducts(products);
        return orderDetail;
    }
}
