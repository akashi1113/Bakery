package com.csu.bakery.persistence;

import com.csu.bakery.model.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CartDao {

    Cart selectCartByUserId(@Param("userId") Long userId);

    int insertCart(Cart cart);

    int updateCart(Cart cart);
}
