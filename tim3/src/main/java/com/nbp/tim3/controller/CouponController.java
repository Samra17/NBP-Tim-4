package com.nbp.tim3.controller;

import com.nbp.tim3.dto.coupon.CouponCreateUpdateRequest;
import com.nbp.tim3.dto.coupon.CouponPaginatedResponse;
import com.nbp.tim3.dto.coupon.CouponResponse;
import com.nbp.tim3.service.CouponService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(path="/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    //@PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Get all coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all coupons",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponPaginatedResponse.class)) })}
    )
    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<CouponPaginatedResponse> getAllCoupons(
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        var coupons = couponService.getAllCoupons(page, size);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @Operation(description = "Get a coupon by coupon ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupon with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<CouponResponse> getCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id) {
        var coupon = couponService.getCouponById(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    // @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Create a new coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<CouponResponse> addNewCoupon(
            @Parameter(description = "Information required for coupon creation", required = true)
            @Valid @RequestBody CouponCreateUpdateRequest couponCreateUpdateRequest) {
        var couponId = couponService.addNewCoupon(couponCreateUpdateRequest);
        return new ResponseEntity<>(couponService.getCouponById(couponId), HttpStatus.CREATED);
    }
//
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update coupon information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated coupon information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<CouponResponse> updateCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Coupon information to be updated", required = true)
            @Valid @RequestBody CouponCreateUpdateRequest couponDto){
        couponService.updateCoupon(couponDto, id);
        return  new ResponseEntity<>(couponService.getCouponById(id), HttpStatus.CREATED);
    }

    /*
//    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a coupon")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the coupon with provided ID"),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id) {
        return new ResponseEntity<>(couponService.deleteCoupon(id), HttpStatus.OK);
    }

     */
//
//    @Operation(description = "Filter restaurants with coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found filtered restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/filter")
    public @ResponseBody ResponseEntity<List<Integer>> filterRestaurants(
            @Parameter(description = "Restaurant UUID list", required = true)
            @RequestBody List<Integer> restaurants) {
        var filteredRestaurants = couponService.filterRestaurants(restaurants);
        return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
    }
//
//    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Use one coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully used coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/apply/{id}")
    public @ResponseBody ResponseEntity<CouponResponse> useCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id) {
        couponService.applyCoupon(id);
        return new ResponseEntity<>(couponService.getCouponById(id), HttpStatus.OK);
    }
//
//    //@PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Get coupons by restaurant ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupons with provided restaurant UUID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponPaginatedResponse.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupons with provided restaurant Id not found",
                    content = @Content)})
    @GetMapping(path = "/res/{restaurantId}")
    public  @ResponseBody ResponseEntity<CouponPaginatedResponse> getCouponForRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Integer restaurantId,
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        var coupons = couponService.getAllCouponsForRestaurant(restaurantId, page, size);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

}
