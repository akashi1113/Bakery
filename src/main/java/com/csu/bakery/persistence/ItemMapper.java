package com.csu.bakery.persistence;

import com.csu.bakery.model.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    List<Item> selectListByKeyWords(String keywords);

    List<Item> selectByProduct(String productId);

    Item getItemById(String itemId);

}