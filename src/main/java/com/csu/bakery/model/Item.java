package com.csu.bakery.model;
import lombok.Data;
@Data
public class Item {
    private String itemId;
    private String name;
    private String productId;
    private double price;
    private String image;
    private String descn;
    private int stock;
    private int supplier;
    private String status;
}
