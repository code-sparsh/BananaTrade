package com.sparshsethi.bananatrade.order.service;

import com.sparshsethi.bananatrade.order.constants.OrderType;
import com.sparshsethi.bananatrade.order.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderService {
    public String createOrder(CreateOrderRequest createOrderRequest) {
        int quantity = createOrderRequest.getQuantity();
        String instrument = createOrderRequest.getInstrument();
        OrderType orderType = createOrderRequest.getOrderType();
        return String.format("Creating a new %s order for instrument: %s with quantity %d", orderType, instrument, quantity);
    }


}
