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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping(path="/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Get all coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all coupons",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponPaginatedResponse.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<CouponPaginatedResponse> getAllCoupons(
            @Parameter(description = "Page number", required = true)
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Number of records per page", required = true)
            @RequestParam(value = "perPage", defaultValue = "10") Integer perPage) {
        var coupons = couponService.getAllCoupons(page, perPage);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @Operation(description = "Get a coupon by coupon code")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupon with provided code",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupon with provided code not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/code/{code}")
    public  @ResponseBody ResponseEntity<CouponResponse> getCouponByCode(
            @Parameter(description = "Coupon Code", required = true)
            @PathVariable String code) {
        var coupon = couponService.getCouponByCode(code);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @Operation(description = "Get a coupon by coupon id")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupon with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<CouponResponse> getCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id) {
        var coupon = couponService.getCouponById(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Create a new coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<CouponResponse> addNewCoupon(
            @Parameter(description = "Information required for coupon creation", required = true)
            @Valid @RequestBody CouponCreateUpdateRequest couponCreateUpdateRequest) {
        var couponId = couponService.addNewCoupon(couponCreateUpdateRequest);
        return new ResponseEntity<>(couponService.getCouponById(couponId), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update coupon information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated coupon information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)}
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<CouponResponse> updateCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Coupon information to be updated", required = true)
            @Valid @RequestBody CouponCreateUpdateRequest couponDto){
        couponService.updateCoupon(couponDto, id);
        return  new ResponseEntity<>(couponService.getCouponById(id), HttpStatus.OK);
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

    @Operation(description = "Filter restaurants based on whether they do or do not contain coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found filtered restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path="/filter")
    public @ResponseBody ResponseEntity<List<Integer>> filterRestaurants(
            @Parameter(description = "Restaurant ID list", required = true)
            @RequestBody List<Integer> restaurants) {
        var filteredRestaurants = couponService.filterRestaurants(restaurants);
        return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Use one coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully used coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path="/apply/{id}")
    public @ResponseBody ResponseEntity<CouponResponse> useCoupon(
            @Parameter(description = "Coupon ID", required = true)
            @PathVariable Integer id) {
        couponService.applyCoupon(id);
        return new ResponseEntity<>(couponService.getCouponById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Get all restaurant's coupons")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupons with provided restaurant ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponPaginatedResponse.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupons with provided restaurant ID not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Unauthorized access",
                    content = @Content)})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/res/{restaurantId}")
    public  @ResponseBody ResponseEntity<CouponPaginatedResponse> getCouponForRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable Integer restaurantId,
            @Parameter(description = "Page number", required = true)
            @RequestHeader(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "Records per page", required = true)
            @RequestHeader(value = "size", defaultValue = "10") Integer size) {
        var coupons = couponService.getAllCouponsForRestaurant(restaurantId, page, size);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

}
