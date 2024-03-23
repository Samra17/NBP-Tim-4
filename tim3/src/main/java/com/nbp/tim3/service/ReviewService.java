package com.nbp.tim3.service;

import com.nbp.tim3.dto.review.ReviewCreateRequest;
import com.nbp.tim3.dto.review.ReviewResponse;
import com.nbp.tim3.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
   // @Autowired
    //private ReviewRepository reviewRepository;

    //@Autowired
    //private RestaurantRepository restaurantRepository;

    public Review addNewReview(ReviewCreateRequest request) {
        /*var existingReview = reviewRepository.findByUserAndRestaurant(request.getUserUUID(),request.getRestaurantId());
        if(existingReview!=null)
        {
            existingReview.setRating(request.getRating());
            existingReview.setComment(request.getComment());
            existingReview.setModified(LocalDateTime.now());
            existingReview.setModifiedBy(request.getUserUUID());
            reviewRepository.save(existingReview);
            return existingReview;
        }
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUserUUID(request.getUserUUID());
        var exception = new EntityNotFoundException("Restaurant with id " + request.getRestaurantId() + " does not exist!");
        var restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow(()-> exception);
        review.setRestaurant(restaurant);
        review.setCreated(LocalDateTime.now());
        review.setCreatedBy(request.getUserUUID());
        reviewRepository.save(review);

        return review;*/

        return new Review();
    }

    public List<ReviewResponse> getReviewsForRestaurant(String restaurantUUID) {
        /*var exception = new EntityNotFoundException("Restaurant with uuid " + restaurantUUID + " does not exist!");
        restaurantRepository.findByUUID(restaurantUUID).orElseThrow(()-> exception);
        return reviewRepository.getReviewsForRestaurant(restaurantUUID);*/

        return new ArrayList<>();
    }

    public List<Review> getUserReviews(String userUUID) {
        // return reviewRepository.getUserReviews(userUUID);

        return new ArrayList<>();
    }


    public String deleteReview(Long id) {
        /*var review = reviewRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Review with id " + id + " does not exist!"));
        reviewRepository.delete(review);
        return "Review with id " + id + " successfully deleted!";*/
        
        return "Something";
    }
}
