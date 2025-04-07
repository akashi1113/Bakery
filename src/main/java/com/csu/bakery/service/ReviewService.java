package com.csu.bakery.service;


import com.csu.bakery.model.Review;
import com.csu.bakery.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    public int insertReview(Review review) {
        return reviewDao.insertReview(review);
    }

    public List<Review> getAllReviews() {
        return reviewDao.getAllReviews();
    }

    public List<Review> getReviewsByItem(String itemid) {
        return reviewDao.getReviewsByItem(itemid);
    }

    public List<Review> getReviewsByUser(Long userid){
        return reviewDao.getReviewsByUser(userid);
    }
}