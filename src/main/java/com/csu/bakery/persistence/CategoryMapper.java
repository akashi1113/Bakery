package com.csu.bakery.persistence;

import com.csu.bakery.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    List<Category> selectAllCategory();

    Category selectById(String categoryId);
}
