package com.nbp.tim3.controller;

import com.nbp.tim3.dto.review.ReviewCreateRequest;
import com.nbp.tim3.dto.review.ReviewResponse;
import com.nbp.tim3.model.Review;
import com.nbp.tim3.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path="/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(description = "Get all reviews of a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all reviews for the restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Restaurant with provided id does not exist",
                    content = @Content)})
    @GetMapping(path="/restaurant/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<ReviewResponse>> getReviewsForRestaurant (
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Integer restaurantId,
            @RequestHeader(value = "page", defaultValue = "0") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        var reviews = reviewService.getReviewsByRestaurantId(restaurantId, page, size);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @Operation(description = "Get all reviews created by a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all reviews for the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "User with provided id does not exist",
                    content = @Content)})
    @GetMapping(path="/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<ReviewResponse>> getReviewsForUser (
            @Parameter(description = "User ID", required = true)
            @PathVariable Integer userId,
            @RequestHeader(value = "page", defaultValue = "0") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        var reviews = reviewService.getReviewsByUserId(userId, page, size);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Create a new review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new review",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<ReviewResponse> addNewReview (
            @Parameter(description = "Information required for review creation", required = true)
            @Valid @RequestBody ReviewCreateRequest request) {

        Integer reviewId = reviewService.addNewReview(request);
        return new ResponseEntity<>(reviewService.getReviewById(reviewId),HttpStatus.CREATED);
    }

//    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Delete a review")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the review with provided ID"),
            @ApiResponse(responseCode = "404", description = "Review with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<String> deleteReview(
            @Parameter(description = "Review ID", required = true)
            @PathVariable Integer id) {

        return new ResponseEntity<>(reviewService.deleteReview(id),HttpStatus.OK);
    }
}

