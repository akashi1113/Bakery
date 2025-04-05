package com.csu.bakery.controller;

import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.model.Item;
import com.csu.bakery.service.CategoryService;
import com.csu.bakery.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@Controller
@RequestMapping("/item/")
public class ItemController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;

    @GetMapping("items/{id}")
    @ResponseBody
    public CommodityResponse<List<Item>> getItemListByProduct(@PathVariable("id") String productId){
        return itemService.getItemListByProduct(productId);
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommodityResponse<Item> getItem(@PathVariable("id") String itemId){
        return itemService.searchItem(itemId);
    }

    @GetMapping("search")
    @ResponseBody
    public CommodityResponse<List<Item>> searchItem(String keywords){
        return itemService.searchItemByKeywords(keywords);
    }

}
