package com.nbp.tim3.controller;

import com.nbp.tim3.dto.coupon.CouponDto;
import com.nbp.tim3.model.Coupon;
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
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequestMapping(path="/coupon") // This means URL's start with /demo (after Application path)
public class CouponController {
    @Autowired
    private CouponService couponService;

    /*
    //@PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Get all coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found all coupons",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) })}
    )
    @GetMapping(path="/all")
    public @ResponseBody ResponseEntity<List<Coupon>> getAllCoupons(@RequestHeader("username") String username) {
        var coupons = couponService.getAllCoupons();
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }


    @Operation(description = "Get a coupon by coupon ID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupon with provided ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)})
    @GetMapping(path = "/{id}")
    public  @ResponseBody ResponseEntity<Coupon> getCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable  Integer id, @RequestHeader("username") String username) {
        var coupon = couponService.getCoupon(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    // @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Create a new coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created a new coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<Coupon> addNewCoupon(@Parameter(description = "Information required for coupon creation", required = true) @Valid @RequestBody CouponDto couponDto, @RequestHeader("username") String username) {
        var coupon = couponService.addNewCoupon(couponDto);
        return  new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Update coupon information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated coupon information",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)}
    )
    @PutMapping(path = "/update/{id}")
    public @ResponseBody ResponseEntity<Coupon> updateCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable Integer id, @Parameter(description = "Coupon information to be updated", required = true) @Valid @RequestBody CouponDto couponDto,@RequestHeader("username") String username){
        var coupon = couponService.updateCoupon(couponDto, id);
        return  new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RESTAURANT_MANAGER')")
    @Operation(description = "Delete a coupon")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the coupon with provided ID"),
            @ApiResponse(responseCode = "404", description = "Coupon with provided ID not found",
                    content = @Content)})
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<String> deleteCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable Integer id, @RequestHeader("username") String username) {
        return new ResponseEntity<>(couponService.deleteCoupon(id), HttpStatus.OK);
    }

    @Operation(description = "Filter restaurants with coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found filtered restaurants",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/filter")
    public @ResponseBody ResponseEntity<List<String>> filterRestaurants(@Parameter(description = "Restaurant UUID list", required = true) @RequestBody List<String> restaurants) {
        var filteredRestaurants = couponService.filterRestaurants(restaurants);
        return new ResponseEntity<>(filteredRestaurants, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Use one coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully used coupon",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid information supplied",
                    content = @Content)})
    @PostMapping(path="/apply/{id}")
    public @ResponseBody ResponseEntity<Integer> useCoupon(@Parameter(description = "Coupon ID", required = true) @PathVariable Integer id, @RequestHeader("username") String username) {
        Integer quantity = couponService.applyCoupon(id);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('CUSTOMER')")
    @Operation(description = "Get coupons by restaurant UUID")
    @ApiResponses ( value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the coupons with provided restaurant UUID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Coupon.class)),
                    }),
            @ApiResponse(responseCode = "404", description = "Coupons with provided restaurant UUID not found",
                    content = @Content)})
    @GetMapping(path = "/res/{uuid}")
    public  @ResponseBody ResponseEntity<List<Coupon>> getCouponForRestaurant(@Parameter(description = "Restaurant UUID", required = true) @PathVariable  String uuid, @RequestHeader("username") String username) {
        var coupon = couponService.getAllCouponsForRestaurant(uuid);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

     */
}
