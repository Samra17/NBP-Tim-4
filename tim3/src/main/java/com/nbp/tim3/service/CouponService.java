package com.nbp.tim3.service;

import com.nbp.tim3.dto.coupon.CouponCreateUpdateRequest;
import com.nbp.tim3.dto.coupon.CouponPaginatedResponse;
import com.nbp.tim3.dto.coupon.CouponResponse;
import com.nbp.tim3.repository.CouponRepository;
import com.nbp.tim3.repository.RestaurantRepository;
import com.nbp.tim3.util.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public CouponPaginatedResponse getAllCoupons(Integer page, Integer size) {
        return couponRepository.getAll(page, size);
    }

    public CouponResponse getCouponById(Integer id) {
        CouponResponse coupon = couponRepository.getById(id);
        if (coupon == null)
            throw new EntityNotFoundException(String.format("Coupon with id %d does not exist!", id));
        return coupon;
    }

    public Integer addNewCoupon(CouponCreateUpdateRequest couponCreateUpdateRequest, String username) {
        var restaurantId = restaurantRepository.getRestaurantIdByManagerUsername(username);
        couponCreateUpdateRequest.setRestaurantId(restaurantId);
        if(couponRepository.getByCode(couponCreateUpdateRequest.getCode(),restaurantId) != null)
            throw new InvalidRequestException(String.format("Coupon with code %s already exists!", couponCreateUpdateRequest.getCode()));
        return couponRepository.createCoupon(couponCreateUpdateRequest);
    }

    public String deleteCoupon(Integer id) {
        if (!couponRepository.deleteCoupon(id))
            throw new EntityNotFoundException(String.format("Coupon item with id %d does not exist!", id));
        return "Coupon with id " + id + " is successfully deleted!";
    }

    public void updateCoupon(Integer quantity, Integer id, String username) {
        boolean updated = couponRepository.updateCoupon(id, quantity);
        if (!updated) {
            throw new EntityNotFoundException(String.format("Coupon with id %d does not exist!", id));
        }
    }

    public List<Integer> filterRestaurants(List<Integer> restaurants) {
        return couponRepository.filterRestaurantsWithCoupons(restaurants);
    }

    public Integer applyCoupon(Integer id) {
        boolean updated = couponRepository.applyCoupon(id);
        if (!updated) {
            throw new EntityNotFoundException(String.format("Coupon with id %d does not exist!", id));
        }

        return 3;
    }

    public CouponPaginatedResponse getAllCouponsForRestaurant(String username, Integer page, Integer size) {
        Integer restaurantId = restaurantRepository.getRestaurantIdByManagerUsername(username);
        return couponRepository.getByRestaurantIdPage(restaurantId, page, size);
    }

    public CouponResponse getCouponByCode(String code, Integer restaurantId) {
        CouponResponse coupon = couponRepository.getByCode(code, restaurantId);
        if (coupon == null)
            throw new EntityNotFoundException(String.format("Coupon with code %s does not exist!", code));
        return coupon;

    }
}
