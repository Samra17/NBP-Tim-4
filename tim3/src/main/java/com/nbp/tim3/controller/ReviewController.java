package com.nbp.tim3.controller;

import com.nbp.tim3.dto.review.ReviewCreateRequest;
import com.nbp.tim3.dto.review.ReviewPaginatedResponse;
import com.nbp.tim3.dto.review.ReviewResponse;
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

@RestController
@RequestMapping(path="/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(description = "Get all restaurant's reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all reviews for the restaurant",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Restaurant with provided id does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @GetMapping(path="/restaurant")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ReviewPaginatedResponse> getReviewsForRestaurant (
            @Parameter(description = "Page number", required = true)
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestHeader(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username) {
        var reviews = reviewService.getReviewsByRestaurantManager(username, page, size);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @Operation(description = "Get all reviews created by a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all reviews for the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "User with provided id does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @GetMapping(path="/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ReviewPaginatedResponse> getReviewsForUser (
            @Parameter(description = "User ID", required = true)
            @PathVariable Integer userId,
            @Parameter(description = "Page number", required = true)
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        var reviews = reviewService.getReviewsByUserId(userId, page, size);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Create a new review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new review",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<ReviewResponse> addNewReview (
            @Parameter(description = "Information required for review creation", required = true)
            @Valid @RequestBody ReviewCreateRequest request,
            @Parameter(description = "User username", required = false)
            @RequestHeader(value = "username", required = false) String username) {

        Integer reviewId = reviewService.addNewReview(request, username);
        return new ResponseEntity<>(reviewService.getReviewById(reviewId),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Delete a review")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the review with provided ID"),
            @ApiResponse(responseCode = "404", description = "Review with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody ResponseEntity<String> deleteReview(
            @Parameter(description = "Review ID", required = true)
            @PathVariable Integer id) {

        reviewService.deleteReview(id);

        return ResponseEntity.noContent().build();
    }
}

