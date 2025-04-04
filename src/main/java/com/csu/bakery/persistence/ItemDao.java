package com.csu.bakery.persistence;

import com.csu.bakery.model.Item;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ItemDao {
    /**
     * 根据 itemId 查询商品信息
     */
    Item selectItemById(String itemId);
}

