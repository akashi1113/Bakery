package com.csu.bakery.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderPrepareResponse {

    private Long cartId;

    private Double totalQuantity;

    private Double subTotal;
    private List<OrderItemResponse> orderItems;
}


