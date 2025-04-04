//package com.csu.bakery.controller;
//
//import com.csu.bakery.config.JwtUtil;
//import com.csu.bakery.dto.CartResponse;
//import com.csu.bakery.dto.OrderAndCartResponse;
//import com.csu.bakery.service.CartService;
//import com.csu.bakery.model.Cart;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.web.bind.annotation.*;
//        import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/cart")
//@RequiredArgsConstructor
//@Slf4j
//public class CartController {
//
//    private final CartService cartService;
//    private final JwtUtil jwtUtil;
//
//    /**
//     * 从请求头中解析 JWT token 获取用户 ID。
//     *
//     * @param tokenHeader 请求头中的 Authorization 字段
//     * @return 用户ID
//     * @throws AuthenticationException 如果 token 缺失、格式错误或无效
//     */
//    private Long getUserIdFromToken(String tokenHeader) throws AuthenticationException {
//        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
//            log.warn("缺少或无效的 Authorization 标头");
//            throw new BadCredentialsException("缺少或无效的 Authorization 标头。");
//        }
//        String token = tokenHeader.replace("Bearer ", "");
//        try {
//            return jwtUtil.extractUserId(token);
//        } catch (Exception e) {
//            log.warn("从 token 提取 userId 失败: {}", e.getMessage());
//            throw new BadCredentialsException("无效或过期的 token: " + e.getMessage(), e);
//        }
//    }
//
//    /**
//     * GET /cart
//     * 获取当前用户的购物车信息。
//     *
//     * @param tokenHeader 请求头中的 Authorization 字段
//     * @return 购物车信息封装在 OrderAndCartResponse 中
//     */
//    @GetMapping
//    public ResponseEntity<OrderAndCartResponse<CartResponse>> getCart(
//            @RequestHeader("Authorization") String tokenHeader) {
//        Long userId = getUserIdFromToken(tokenHeader);
//        Cart cart = cartService.getCartByUserId(userId);
//        CartResponse response = new CartResponse(cart);
//        return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(response));
//    }
//
//    /**
//     * POST /cart/item
//     * 添加商品到购物车（若商品已存在则增加数量）。
//     *
//     * @param tokenHeader 请求头中的 Authorization 字段
//     * @param itemId      商品ID
//     * @param quantity    添加数量
//     * @param price       商品单价
//     * @return 更新后的购物车信息封装在 OrderAndCartResponse 中
//     */
//    @PostMapping("/item")
//    public ResponseEntity<OrderAndCartResponse<CartResponse>> addItemToCart(
//            @RequestHeader("Authorization") String tokenHeader,
//            @RequestParam("itemId") Long itemId,
//            @RequestParam("quantity") Integer quantity,
//            @RequestParam("price") Double price) {
//        Long userId = getUserIdFromToken(tokenHeader);
//        Cart cart = cartService.addItemToCart(userId, itemId, quantity, price);
//        CartResponse response = new CartResponse(cart);
//        return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(response));
//    }
//
//    /**
//     * PUT /cart/item
//     * 更新购物车中指定商品的数量。
//     *
//     * @param tokenHeader 请求头中的 Authorization 字段
//     * @param itemId      商品ID
//     * @param quantity    新的商品数量
//     * @param price       商品单价
//     * @return 更新后的购物车信息封装在 OrderAndCartResponse 中
//     */
//    @PutMapping("/item")
//    public ResponseEntity<OrderAndCartResponse<CartResponse>> updateItemQuantity(
//            @RequestHeader("Authorization") String tokenHeader,
//            @RequestParam("itemId") Long itemId,
//            @RequestParam("quantity") Integer quantity,
//            @RequestParam("price") Double price) {
//        Long userId = getUserIdFromToken(tokenHeader);
//        Cart cart = cartService.updateItemQuantity(userId, itemId, quantity, price);
//        CartResponse response = new CartResponse(cart);
//        return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(response));
//    }
//
//    /**
//     * DELETE /cart/item
//     * 从购物车中移除指定商品。
//     *
//     * @param tokenHeader 请求头中的 Authorization 字段
//     * @param itemId      要删除的商品ID
//     * @return 更新后的购物车信息封装在 OrderAndCartResponse 中
//     */
//    @DeleteMapping("/item")
//    public ResponseEntity<OrderAndCartResponse<CartResponse>> removeItemFromCart(
//            @RequestHeader("Authorization") String tokenHeader,
//            @RequestParam("itemId") Long itemId) {
//        Long userId = getUserIdFromToken(tokenHeader);
//        Cart cart = cartService.removeItemFromCart(userId, itemId);
//        CartResponse response = new CartResponse(cart);
//        return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(response));
//    }
//
//    /**
//     * DELETE /cart
//     * 清空当前用户的购物车。
//     *
//     * @param tokenHeader 请求头中的 Authorization 字段
//     * @return 清空成功信息封装在 OrderAndCartResponse 中
//     */
//    @DeleteMapping
//    public ResponseEntity<OrderAndCartResponse<String>> clearCart(
//            @RequestHeader("Authorization") String tokenHeader) {
//        Long userId = getUserIdFromToken(tokenHeader);
//        cartService.clearCart(userId);
//        return ResponseEntity.ok(OrderAndCartResponse.createForSuccess("购物车已清空"));
//    }
//}
