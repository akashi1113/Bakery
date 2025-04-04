package com.csu.bakery.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private String itemId;

    private Integer quantity;

    private Double totalPrice;
}

