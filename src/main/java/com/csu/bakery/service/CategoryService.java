package com.csu.bakery.service;

import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.model.Category;
import com.csu.bakery.persistence.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public CommodityResponse<List<Category>> getCategoryList() {

        List<Category> categoryList=categoryMapper.selectAllCategory();
        if(categoryList.isEmpty()){
            return CommodityResponse.createForSuccessMessage("没有分类信息");
        }
        return CommodityResponse.createForSuccess(categoryList);

    }

    public CommodityResponse<Category> getById(String categoryId) {
        Category category=categoryMapper.selectById(categoryId);
        if(category==null){
            return CommodityResponse.createForSuccessMessage("没有该id的分类信息");
        }
        return CommodityResponse.createForSuccess(category);
    }
}
