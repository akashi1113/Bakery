package com.csu.bakery.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
 public class LineItem implements Serializable {
       private Long orderId;
       private Long cartId;
       private int quantity;
       private String itemId;
       private double totalPrice;

}
