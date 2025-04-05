package com.csu.bakery.persistence;

import com.csu.bakery.model.Review;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReviewDao {
    public List<Review> getAllReviews();
    public List<Review> getReviewsByItem(String itemid);
    public List<Review> getReviewsByUser(String userid);
}
