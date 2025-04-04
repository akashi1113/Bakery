package com.csu.bakery.persistence;

import com.csu.bakery.model.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CartDao {
    /**
     * 根据 userId 查询购物车
     */
    Cart selectCartByUserId(@Param("userId") Long userId);

    /**
     * 插入购物车记录
     */
    int insertCart(Cart cart);

    /**
     * 更新购物车（更新总价、总数量）
     */
    int updateCart(Cart cart);
}
