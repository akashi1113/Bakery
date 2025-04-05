package com.csu.bakery.service;

import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.model.Item;
import com.csu.bakery.persistence.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("itemService")
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    public CommodityResponse<Item> searchItem(String itemId){
        Item item=itemMapper.getItemById(itemId);
        if(item==null){
            return CommodityResponse.createForSuccessMessage("没有该Item");
        }
        return CommodityResponse.createForSuccess(item);
    }

    public CommodityResponse<List<Item>> getItemListByProduct(String productId) {
        List<Item> items=itemMapper.selectByProduct(productId);
        if(items.isEmpty()){
            return CommodityResponse.createForSuccessMessage("没有Item信息");
        }
        return CommodityResponse.createForSuccess(items);
    }

    public CommodityResponse<List<Item>> searchItemByKeywords(String keywords) {
        List<Item> items=itemMapper.selectListByKeyWords(keywords);
        if(items.isEmpty()){
            return CommodityResponse.createForSuccessMessage("没有Item信息");
        }
        return CommodityResponse.createForSuccess(items);
    }
}
