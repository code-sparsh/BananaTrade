package com.sparshsethi.bananatrade.order.controller;

import com.sparshsethi.bananatrade.order.constants.OrderType;
import com.sparshsethi.bananatrade.order.dto.CreateOrderRequest;
import com.sparshsethi.bananatrade.order.service.CreateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private CreateOrderService createOrderService;

    @GetMapping
    public String test() {
        return "Hello World from BananaTrade!";
    }

    @PostMapping("/create")
    public String createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return createOrderService.createOrder(createOrderRequest);
    }
}
