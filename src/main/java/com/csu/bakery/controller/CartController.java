package com.csu.bakery.controller;

import com.csu.bakery.config.JwtUtil;
import com.csu.bakery.dto.CartItemResponse;
import com.csu.bakery.dto.CartResponse;
import com.csu.bakery.dto.OrderAndCartResponse;
import com.csu.bakery.model.Cart;
import com.csu.bakery.model.CartItem;
import com.csu.bakery.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;

    private Long getUserIdFromToken(String tokenHeader) throws AuthenticationException {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            log.warn("缺少或无效的 Authorization 标头");
            throw new BadCredentialsException("缺少或无效的 Authorization 标头。");
        }
        String token = tokenHeader.replace("Bearer ", "");
        try {
            return jwtUtil.extractUserId(token);
        } catch (Exception e) {
            log.warn("从 token 提取 userId 失败: {}", e.getMessage());
            throw new BadCredentialsException("无效或过期的 token: " + e.getMessage(), e);
        }
    }

    /** 将 CartItem 转为只包含前端需要字段的 DTO */
    private CartItemResponse toDto(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.setItemId(item.getItemId());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    /** 将 Cart 转为 CartResponse，只暴露需要的字段 */
    private CartResponse toCartResponse(Cart cart) {
        CartResponse resp = new CartResponse();
        resp.setCartId(cart.getCartId());
//        resp.setUserId(cart.getUserId());
        resp.setTotalQuantity(cart.getTotalQuantity());
        resp.setSubTotal(cart.getSubTotal());
        // 只转换需要的字段
        List<CartItemResponse> dtoItems = cart.getItems().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        resp.setItems(dtoItems);
        return resp;
    }

    @GetMapping
    public ResponseEntity<OrderAndCartResponse<CartResponse>> getCart(
            @RequestHeader("Authorization") String tokenHeader) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<OrderAndCartResponse<CartResponse>> addItemToCart(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("itemId") String itemId,
            @RequestParam("quantity") Integer quantity){
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.addItemToCart(userId, itemId, quantity);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<OrderAndCartResponse<CartResponse>> updateItemQuantity(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("itemId") String itemId,
            @RequestParam("newquantity") Integer newquantity) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.updateItemQuantity(userId, itemId, newquantity);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<OrderAndCartResponse<CartResponse>> removeItemFromCart(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("itemId") String itemId) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

//    @DeleteMapping
//    public ResponseEntity<OrderAndCartResponse<String>> clearCart(
//            @RequestHeader("Authorization") String tokenHeader) {
//        Long userId = getUserIdFromToken(tokenHeader);
//        Cart cart=cartService.clearCart(userId);
//        return ResponseEntity.ok(
//                OrderAndCartResponse.createForSuccess("购物车已清空")
//        );
//    }
    @DeleteMapping
    public ResponseEntity<OrderAndCartResponse<CartResponse>> clearCart(
            @RequestHeader("Authorization") String tokenHeader) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart=cartService.clearCart(userId);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess("购物车已清空",toCartResponse(cart))
        );
    }
}
