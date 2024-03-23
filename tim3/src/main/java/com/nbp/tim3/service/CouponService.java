package com.nbp.tim3.service;

import com.nbp.tim3.dto.coupon.CouponDto;
import com.nbp.tim3.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {
    //@Autowired
    // private CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        // return new ArrayList<>(couponRepository.findAll());
        return new ArrayList<>();
    }

    public Coupon getCoupon(Integer id) {
        /*var exception = new EntityNotFoundException("Coupon with id " + id + " does not exist!");
        var coupon = couponRepository.findById(id);
        return coupon.orElseThrow(() -> exception);*/

        return new Coupon();
    }

    public Coupon addNewCoupon(CouponDto couponDto) {
        /*Coupon coupon = new Coupon(couponDto);
        couponRepository.save(coupon);
        return coupon;*/

        return new Coupon();
    }

    public String deleteCoupon(Integer id) {
        /*var coupon = couponRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Coupon with id " + id + " does not exist!"));
        couponRepository.deleteById(id);
        return "Coupon with id " + id + " is successfully deleted!";*/

        return "Something";
    }

    public Coupon updateCoupon(CouponDto couponDto, Integer id) {
        /*var exception = new EntityNotFoundException("Coupon with id " + id + " does not exist!");
        var coupon = couponRepository.findById(id).orElseThrow(() -> exception);
        coupon.setCode(couponDto.getCode());
        coupon.setCoupon_uuid(couponDto.getCoupon_uuid());
        coupon.setQuantity(couponDto.getQuantity());
        coupon.setDiscount_percentage(couponDto.getDiscount_percentage());
        coupon.setRestaurant_uuid(couponDto.getRestaurant_uuid());
        couponRepository.save(coupon);
        return coupon;*/

        return new Coupon();
    }

    public List<String> filterRestaurants(List<String> restaurants) {
        /*List<String> reList = couponRepository.findAll().stream().map(Coupon::getRestaurant_uuid).collect(Collectors.toList());
        return restaurants.stream().filter(reList::contains).collect(Collectors.toList());*/

        return new ArrayList<>();
    }

    public Integer applyCoupon(Integer id) {
        /*var exception = new EntityNotFoundException("Coupon with id " + id + " does not exist!");
        var coupon = couponRepository.findById(id);
        if (coupon.isPresent()) {
            coupon.get().setQuantity(coupon.get().getQuantity() - 1);
            couponRepository.save(coupon.get());
            return coupon.get().getQuantity();
        }
        else
            throw exception;*/

        return 3;
    }

    public List<Coupon> getAllCouponsForRestaurant(String restaurant_uuid) {
        // return new ArrayList<>(couponRepository.findAll().stream().filter(coupon -> coupon.getRestaurant_uuid().equals(restaurant_uuid)).toList());

        return new ArrayList<>();
    }
}
