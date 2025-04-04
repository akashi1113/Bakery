//package com.csu.bakery.dto;
//
//import com.csu.bakery.model.Cart;
//import lombok.Data;
//
//import java.math.BigDecimal; // Add import
//
//@Data
//public class CartResponse {
//    private Long cartId;
//    private Long userId;
//    private Long totalQuantity;
//    private BigDecimal subTotal; // Changed from Long to BigDecimal
//
//    public CartResponse(Cart cart) {
//        this.cartId = cart.getCartId();
//        this.userId = cart.getUserId();
//        this.totalQuantity = cart.getTotalQuantity();
//        this.subTotal = cart.getSubTotal(); // Now gets BigDecimal
//    }
//}
