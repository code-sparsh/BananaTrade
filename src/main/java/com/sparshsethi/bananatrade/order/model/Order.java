package com.sparshsethi.bananatrade.order.model;

import com.sparshsethi.bananatrade.order.constants.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Order {

    private String orderId;
    private String instrument;
    private OrderType orderType;
    private int quantity;
    //    private int price;
}
