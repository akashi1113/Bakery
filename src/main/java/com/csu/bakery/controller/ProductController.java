package com.csu.bakery.controller;

import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.model.Category;
import com.csu.bakery.model.Product;
import com.csu.bakery.service.CategoryService;
import com.csu.bakery.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("products/{id}")
    @ResponseBody
    public CommodityResponse<List<Product>> getProductListByCategory(@PathVariable("id") String categoryId){
        return productService.getProductListByCategory(categoryId);
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommodityResponse<Product> getProduct(@PathVariable("id") String productId){
        return productService.getProduct(productId);
    }
}
