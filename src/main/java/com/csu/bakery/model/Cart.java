package com.csu.bakery.model;

import lombok.Data;

import java.math.BigDecimal; // Add import
import java.util.List;

@Data
public class Cart {
    private Long cartId;
    private Long userId;
    private double subTotal;
    private double totalQuantity;
    private List<CartItem> items;

}
