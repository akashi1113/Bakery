package com.csu.bakery.model;

import lombok.Data;

import java.util.Date;

@Data
public class OrderInfo {
    private Long orderId;
    private Date orderDate;
    private Double grandTotal;
}
