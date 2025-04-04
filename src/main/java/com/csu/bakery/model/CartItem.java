package com.csu.bakery.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CartItem {
    private Long cartId;
    private String itemId;
    private Integer quantity;
    private Double totalPrice;
    private double price;
}
