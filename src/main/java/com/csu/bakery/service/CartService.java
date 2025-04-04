//package com.csu.bakery.service;
//import com.csu.bakery.dto.CartResponse;
//import com.csu.bakery.model.Cart;
//import com.csu.bakery.model.LineItem;
//import com.csu.bakery.persistence.CartDao;
//import com.csu.bakery.persistence.LineItemDao;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.NoSuchElementException;
//
///**
// * CartService 负责处理购物车相关的业务逻辑，主要功能包括：
// * <ul>
// *     <li>获取当前用户的购物车信息，如果不存在则创建新的购物车；</li>
// *     <li>向购物车中添加商品或更新已有商品的数量和价格；</li>
// *     <li>更新购物车中商品数量，若数量为 0 则删除对应商品；</li>
// *     <li>移除购物车中的指定商品；</li>
// *     <li>清空购物车中的所有商品；</li>
// *     <li>每次修改后重新计算购物车中的总数量和小计金额，并更新数据库中的购物车数据。</li>
// * </ul>
// *
// * 说明：
// * <ul>
// *     <li>该服务依赖 CartDao 和 LineItemDao 数据访问对象进行数据库操作；</li>
// *     <li>业务层代码遵循了事务性操作的原则，确保数据的一致性；</li>
// *     <li>对于金额计算请注意数据类型的选择和精度问题，根据实际业务需求可选择 BigDecimal。</li>
// * </ul>
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CartService {
//
//    private final CartDao cartDao;
//    private final LineItemDao lineItemDao;
//
//    /**
//     * 获取当前用户的购物车。
//     * <p>
//     * 如果用户的购物车不存在，则创建一个新的购物车并写入数据库。
//     *
//     * @param userId 当前用户ID
//     * @return Cart 当前用户的购物车实体
//     */
//    public Cart getCartByUserId(Long userId) {
//        Cart cart = cartDao.selectCartByUserId(userId);
//        if (cart == null) {
//            // 如果没有购物车数据，创建一个新的购物车
//            cart = new Cart();
//            cart.setUserId(userId);
//            cart.setTotalQuantity((long)0);
//            cart.setSubTotal(0);
//            int rows = cartDao.insertCart(cart);
//            if (rows <= 0) {
//                log.error("创建购物车失败，用户ID: {}", userId);
//                throw new IllegalStateException("创建购物车失败");
//            }
//            log.info("成功创建购物车，用户ID: {}", userId);
//        }
//        return cart;
//    }
//
//    /**
//     * 向购物车中添加商品或更新已存在商品的数量与总价。
//     * <p>
//     * 该方法包括以下步骤：
//     * <ul>
//     *     <li>获取用户购物车；</li>
//     *     <li>检查购物车中是否已存在该商品；</li>
//     *     <li>如果存在，更新商品数量和总价；</li>
//     *     <li>如果不存在，新增购物车项；</li>
//     *     <li>重新计算购物车的总数量与小计金额，并更新购物车信息。</li>
//     * </ul>
//     *
//     * @param userId   当前用户ID
//     * @param itemId   商品ID
//     * @param quantity 添加数量
//     * @param price    单价（用于计算总价）
//     * @return Cart 更新后的购物车实体
//     */
//    @Transactional
//    public Cart addItemToCart(Long userId, Long itemId, Integer quantity, Double price) {
//        Cart cart = getCartByUserId(userId);
//        // 查询购物车中的所有商品项
//        List<LineItem> items = lineItemDao.selectLineItemsByCartId(cart.getCartId());
//        LineItem existingItem = items.stream()
//                .filter(item -> item.getItemId().equals(itemId))
//                .findFirst()
//                .orElse(null);
//
//        if (existingItem != null) {
//            // 如果商品已存在，更新数量和总价
//            existingItem.setQuantity(existingItem.getQuantity() + quantity);
//            existingItem.setTotalPrice(existingItem.getTotalPrice() + price * quantity);
//            int updated = lineItemDao.updateLineItem(existingItem);
//            if (updated <= 0) {
//                log.error("更新购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
//                throw new IllegalStateException("更新购物车项失败");
//            }
//        } else {
//            // 如果商品不存在，新增购物车项
//            LineItem newItem = new LineItem();
//            newItem.setCartId(cart.getCartId());
//            newItem.setItemId(itemId);
//            newItem.setQuantity(quantity);
//            newItem.setTotalPrice(price * quantity);
//            int inserted = lineItemDao.insertLineItem(newItem);
//            if (inserted <= 0) {
//                log.error("新增购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
//                throw new IllegalStateException("新增购物车项失败");
//            }
//        }
//        // 重新计算并更新购物车合计数据
//        recalcCart(cart);
//        return cart;
//    }
//
//    /**
//     * 更新购物车中指定商品的数量。
//     * <p>
//     * 如果更新后的数量为 0 或更小，则删除该购物车项。
//     *
//     * @param userId     当前用户ID
//     * @param itemId     商品ID
//     * @param newQuantity 更新后的数量
//     * @param price      单价（用于计算总价）
//     * @return Cart 更新后的购物车实体
//     */
//    @Transactional
//    public Cart updateItemQuantity(Long userId, Long itemId, Integer newQuantity, Double price) {
//        Cart cart = getCartByUserId(userId);
//        LineItem item = lineItemDao.selectLineItemByCartIdAndItemId(cart.getCartId(), itemId);
//        if (item == null) {
//            throw new NoSuchElementException("购物车中不存在该商品，商品ID：" + itemId);
//        }
//        if (newQuantity <= 0) {
//            // 数量为 0 或负数时删除该购物车项
//            int deleted = lineItemDao.deleteLineItemByCartIdAndItemId(cart.getCartId(), itemId);
//            if (deleted <= 0) {
//                log.error("删除购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
//                throw new IllegalStateException("删除购物车项失败");
//            }
//        } else {
//            // 更新数量和总价
//            item.setQuantity(newQuantity);
//            item.setTotalPrice(price * newQuantity);
//            int updated = lineItemDao.updateLineItem(item);
//            if (updated <= 0) {
//                log.error("更新购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
//                throw new IllegalStateException("更新购物车项失败");
//            }
//        }
//        // 重新计算购物车合计数据
//        recalcCart(cart);
//        return cart;
//    }
//
//    /**
//     * 从购物车中移除指定商品。
//     *
//     * @param userId 当前用户ID
//     * @param itemId 要移除的商品ID
//     * @return Cart 更新后的购物车实体
//     */
//    @Transactional
//    public Cart removeItemFromCart(Long userId, Long itemId) {
//        Cart cart = getCartByUserId(userId);
//        int deleted = lineItemDao.deleteLineItemByCartIdAndItemId(cart.getCartId(), itemId);
//        if (deleted <= 0) {
//            log.warn("购物车中未找到要删除的商品，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
//        }
//        // 重新计算购物车合计数据
//        recalcCart(cart);
//        return cart;
//    }
//
//    /**
//     * 清空当前用户的购物车，删除所有购物车项，并重置合计数据。
//     *
//     * @param userId 当前用户ID
//     */
//    @Transactional
//    public void clearCart(Long userId) {
//        Cart cart = getCartByUserId(userId);
//        int deleted = lineItemDao.deleteLineItemsByCartId(cart.getCartId());
//        if (deleted < 0) {
//            log.error("清空购物车失败，购物车ID: {}", cart.getCartId());
//            throw new IllegalStateException("清空购物车失败");
//        }
//        // 重置购物车合计数据
//        cart.setTotalQuantity((long)0);
//        cart.setSubTotal((long)0);
//        int updated = cartDao.updateCart(cart);
//        if (updated <= 0) {
//            log.error("更新购物车数据失败，购物车ID: {}", cart.getCartId());
//            throw new IllegalStateException("更新购物车数据失败");
//        }
//        log.info("成功清空购物车，购物车ID: {}", cart.getCartId());
//    }
//
//    /**
//     * 重新计算购物车中的总商品数量和小计金额，并更新购物车数据。
//     * <p>
//     * 此方法通过查询该购物车下所有购物车项进行计算。
//     *
//     * @param cart 当前购物车实体
//     */
//    private void recalcCart(Cart cart) {
//        List<LineItem> items = lineItemDao.selectLineItemsByCartId(cart.getCartId());
//        int totalQuantity = items.stream().mapToInt(LineItem::getQuantity).sum();
//        double subTotal = items.stream().mapToDouble(LineItem::getTotalPrice).sum();
//        cart.setTotalQuantity(totalQuantity);
//        cart.setSubTotal(subTotal);
//        int updated = cartDao.updateCart(cart);
//        if (updated <= 0) {
//            log.error("更新购物车合计数据失败，购物车ID: {}", cart.getCartId());
//            throw new IllegalStateException("更新购物车合计数据失败");
//        }
//    }
//}
