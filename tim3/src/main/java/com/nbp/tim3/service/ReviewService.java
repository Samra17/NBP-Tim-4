package com.nbp.tim3.service;

import com.nbp.tim3.dto.review.ReviewCreateRequest;
import com.nbp.tim3.dto.review.ReviewPaginatedResponse;
import com.nbp.tim3.dto.review.ReviewResponse;
import com.nbp.tim3.repository.RestaurantRepository;
import com.nbp.tim3.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    public Integer addNewReview(ReviewCreateRequest request) {
        return reviewRepository.createReview(request);
    }

    public ReviewResponse getReviewById(Integer id) {
        return reviewRepository.getReviewById(id);
    }

    public ReviewPaginatedResponse getReviewsByRestaurantManager(String username, Integer page, Integer size) {
        Integer restaurantId = restaurantRepository.getRestaurantIdByManagerUsername(username);
        if(restaurantId == null)
            throw new EntityNotFoundException("Restaurant with id " + restaurantId + " does not exist!");
        return reviewRepository.getByRestaurantIdPage(restaurantId, page, size);
    }

    public ReviewPaginatedResponse getReviewsByUserId(Integer userId, Integer page, Integer size) {
        return reviewRepository.getByUserIdPage(userId, page, size);
    }


    public String deleteReview(Integer id) {
        if (!reviewRepository.deleteReview(id))
            throw new EntityNotFoundException(String.format("Review item with id %d does not exist!", id));
        return "Review with id " + id + " is successfully deleted!";
    }
}
