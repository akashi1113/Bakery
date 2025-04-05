package com.csu.bakery.controller;
import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.model.Category;
import com.csu.bakery.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/category/")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("categories")
    @ResponseBody
    public CommodityResponse<List<Category>> getCategoryList(){
        return categoryService.getCategoryList();
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommodityResponse<Category> getCategory(@PathVariable("id") String categoryId){
        return categoryService.getById(categoryId);
    }
}