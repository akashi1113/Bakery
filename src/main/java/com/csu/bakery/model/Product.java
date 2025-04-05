package com.csu.bakery.model;

import lombok.Data;

@Data
public class Product {
    private String productId;

    private String categoryId;

    private String name;

    private String image;
}
