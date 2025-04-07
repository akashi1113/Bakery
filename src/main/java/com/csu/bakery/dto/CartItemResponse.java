package com.csu.bakery.dto;

import lombok.Data;

@Data
public class CartItemResponse {
    private String itemId;
    private Integer quantity;
    private Double totalPrice;
}
