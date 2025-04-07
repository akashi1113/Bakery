package com.csu.bakery.dto;

import com.csu.bakery.model.Cart;
import com.csu.bakery.model.CartItem;
import lombok.Data;

import java.util.List;


@Data
public class CartResponse {
    private Long cartId;
    private Double totalQuantity;
    private Double subTotal;
    private List<CartItemResponse> items;
}
