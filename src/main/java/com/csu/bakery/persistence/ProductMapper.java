package com.csu.bakery.persistence;

import com.csu.bakery.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository()
public interface ProductMapper {
    Product selectById(String productId);

    List<Product> selectByCategory(String categoryId);

}
