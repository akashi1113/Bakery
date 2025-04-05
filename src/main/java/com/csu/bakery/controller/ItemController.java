package com.csu.bakery.controller;

import com.csu.bakery.common.CommodityResponse;
import com.csu.bakery.config.JwtUtil;
import com.csu.bakery.controller.enums.AccountResponseCode;
import com.csu.bakery.dto.AccountResponse;
import com.csu.bakery.dto.ReviewRequest;
import com.csu.bakery.model.Account;
import com.csu.bakery.model.Item;
import com.csu.bakery.model.Review;
import com.csu.bakery.service.AccountService;
import com.csu.bakery.service.CategoryService;
import com.csu.bakery.service.ItemService;
import com.csu.bakery.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@Controller
@RequestMapping("/item/")
public class ItemController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("items/{id}")
    @ResponseBody
    public CommodityResponse<List<Item>> getItemListByProduct(@PathVariable("id") String productId){
        return itemService.getItemListByProduct(productId);
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommodityResponse<Item> getItem(@PathVariable("id") String itemId){
        return itemService.searchItem(itemId);
    }

    @GetMapping("{id}/reviews")
    @ResponseBody
    public ResponseEntity<List<Review>> getItemReviews(@PathVariable("id") String itemId) {
        List<Review> reviews = reviewService.getReviewsByItem(itemId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("{id}/{userid}")
    @ResponseBody
    public ResponseEntity<AccountResponse<?>> insertReviews(
            @RequestHeader("Authorization") String token,
            @PathVariable("userid") Long userid,
            @PathVariable("id") String itemId,
            @RequestBody ReviewRequest reviewRequest) {
        Account account = accountService.getAccountById(userid);
        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND,null));
        }

        //权限校验
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""),userid,account.getUsername(),account.getTokenVersion())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        // 验证评价内容
        if((reviewRequest.getComment() == null) || reviewRequest.getComment().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AccountResponse.error(AccountResponseCode.PARAM_INVALID, "评价内容不能为空"));
        }
        Review review = new Review();
        review.setUserid(userid);
        review.setItemid(itemId);
        review.setComment(reviewRequest.getComment());
        review.setScore(reviewRequest.getScore());
        int i=reviewService.insertReview(review);
        if(i <= 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AccountResponse.error(AccountResponseCode.SERVER_ERROR, "评价提交失败"));
        }

        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.SUCCESS, review));
    }

    @GetMapping("search")
    @ResponseBody
    public CommodityResponse<List<Item>> searchItem(String keywords){
        return itemService.searchItemByKeywords(keywords);
    }

}
