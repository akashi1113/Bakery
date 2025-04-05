package com.csu.bakery.service;


import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.model.Product;
import com.csu.bakery.persistence.CategoryMapper;
import com.csu.bakery.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public CommodityResponse<Product> getProduct(String productId) {
        // 查询产品信息
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return CommodityResponse.createForSuccessMessage("没有该Product");
        }
        return CommodityResponse.createForSuccess(product);
    }

    public CommodityResponse<List<Product>> getProductListByCategory(String categoryId) {
        List<Product> productList=productMapper.selectByCategory(categoryId);
        if (productList == null) {
            return CommodityResponse.createForSuccessMessage("没有Product信息");
        }
        return CommodityResponse.createForSuccess(productList);
    }
}
