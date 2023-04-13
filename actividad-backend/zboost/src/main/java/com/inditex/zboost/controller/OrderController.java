package com.inditex.zboost.controller;

import com.inditex.zboost.entity.Order;
import com.inditex.zboost.entity.OrderDetail;
import com.inditex.zboost.exception.InvalidParameterException;
import com.inditex.zboost.service.OrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * TODO: EXERCISE 2.a) List orders with limit parameter.
     *
     * Check the api folder for the orders endpoint specification and implement the
     * corresponding REST method,
     * which in turn will call the corresponding service to continue with the call
     * logic.
     *
     * As a good practice, it is recommended to perform validation of the input
     * parameters of requests in the
     * external layer of the architecture, so this would be a good place to validate
     * what is commented on in
     * the exercise statement ;)
     *
     * HINT: If the limit is not valid, you can use the InvalidParameterException to
     * notify it.
     */

    @GetMapping("/orders")
    public List<Order> getOrders(@RequestParam int limit){
        if(limit>100 || limit<1){
            throw new InvalidParameterException("limit="+limit, "Limit out of bounds");
        }
        return orderService.findOrders(limit);
    }

    @GetMapping("/orders/{orderId}")
    public OrderDetail getProductDetail(@PathVariable(name = "orderId", required = true) long orderId) {
        return orderService.findOrderDetail(orderId);
    }
}
