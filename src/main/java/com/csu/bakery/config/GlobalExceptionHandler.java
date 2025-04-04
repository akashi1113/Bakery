package com.csu.bakery.config;

import com.csu.bakery.controller.enums.AccountResponseCode;
import com.csu.bakery.dto.AccountResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.csu.bakery.controller.enums.OrderAndCartResponseCode;
import com.csu.bakery.dto.OrderAndCartResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // --- 用户模块异常处理 (返回 AccountResponse) ---
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AccountResponse<String>> handleValidationException(ConstraintViolationException ex) {
        // 获取第一个错误信息
        String firstError = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(v -> v.getMessage())
                .orElse("Invalid parameter");

        // 返回错误信息
        return ResponseEntity.badRequest()
                .body(AccountResponse.error(AccountResponseCode.PARAM_INVALID, firstError));
    }

    // --- 订单和购物车模块异常处理 (返回 OrderAndCartResponse) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ResponseBody
    public OrderAndCartResponse<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error (RequestBody): {}", errors);
        return OrderAndCartResponse.badRequest(OrderAndCartResponseCode.INVALID_INPUT, "输入参数无效: " + errors);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ResponseBody
    public OrderAndCartResponse<Object> handleBindException(BindException ex) {
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Binding error: {}", errors);
        return OrderAndCartResponse.badRequest(OrderAndCartResponseCode.INVALID_INPUT, "请求参数绑定错误: " + errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ResponseBody
    public OrderAndCartResponse<Object> handleNoSuchElementException(NoSuchElementException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        // Determine the specific 'not found' type based on the message, if possible
        OrderAndCartResponseCode code = OrderAndCartResponseCode.ORDER_NOT_FOUND; // Default
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("购物车")) {
                code = OrderAndCartResponseCode.CART_NOT_FOUND;
            } else if (ex.getMessage().contains("商品") || ex.getMessage().contains("item")) {
                code = OrderAndCartResponseCode.ITEM_NOT_IN_CART;
            }
        }
        return OrderAndCartResponse.notFound(code, ex.getMessage()); // Pass message from exception
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Often maps to 400 or 409 Conflict
    @ResponseBody
    public OrderAndCartResponse<Object> handleIllegalStateException(IllegalStateException ex) {
        log.warn("Illegal state detected: {}", ex.getMessage());
        // Map to specific bad request codes based on message content
        OrderAndCartResponseCode code = OrderAndCartResponseCode.INVALID_INPUT; // Default
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("库存不足")) {
                code = OrderAndCartResponseCode.OUT_OF_STOCK;
            }
        }
        return OrderAndCartResponse.badRequest(code, ex.getMessage()); // Pass message
    }

    @ExceptionHandler(ConcurrencyFailureException.class) // Optimistic Locking Failure
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ResponseBody
    public OrderAndCartResponse<Object> handleConcurrencyFailureException(ConcurrencyFailureException ex) {
        log.warn("Concurrency failure: {}", ex.getMessage());
        // Use a code indicating conflict or needing retry
        return OrderAndCartResponse.error(OrderAndCartResponseCode.OUT_OF_STOCK.getCode(), "资源状态已改变，请重试");
    }

    // --- General Server Errors ---
    @ExceptionHandler(Exception.class) // Fallback for unexpected errors
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ResponseBody
    public OrderAndCartResponse<Object> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: ", ex); // Log the full stack trace
        return OrderAndCartResponse.serverError(); // Return generic server error response
    }

    @ExceptionHandler(RuntimeException.class) // Catch RuntimeExceptions specifically (like from OrderService)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ResponseBody
    public OrderAndCartResponse<Object> handleRuntimeException(RuntimeException ex) {
        log.error("A runtime error occurred: {}", ex.getMessage(), ex); // Log stack trace
        return OrderAndCartResponse.error(OrderAndCartResponseCode.SERVER_ERROR.getCode(), ex.getMessage());
    }
}
