package com.akswi.akswi.service;

import com.akswi.akswi.entity.Category;
import com.akswi.akswi.entity.Coupon;
import com.akswi.akswi.repository.CategoryRepository;
import com.akswi.akswi.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CategoryRepository categoryRepository;



    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public Coupon createCoupon(Coupon coupon) {

        coupon.setCode(coupon.getCode());
        //       coupon.setDiscount(BigDecimal.valueOf(couponRequest.getDiscount()));
        coupon.setDiscountType(coupon.getDiscountType());
        coupon.setDiscountValue(coupon.getDiscountValue());
        coupon.setStartDate(coupon.getStartDate());
        coupon.setEndDate(coupon.getEndDate());
        coupon.setExpiryDate(coupon.getExpiryDate());
        coupon.setUsageLimit(coupon.getUsageLimit());
        coupon.setUsedCount(0);

        // Process the categoryIds
        if (coupon.getCategoryIds() != null && !coupon.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (Long catId : coupon.getCategoryIds()) {
                Category category = categoryRepository.findById(catId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + catId));
                categories.add(category);
            }
            coupon.setCategories(categories);
        } else {
            coupon.setCategories(new HashSet<>());
        }

        Coupon savedCoupon = couponRepository.save(coupon);
        // Debug print
        System.out.println("Coupon saved with categories: " + savedCoupon.getCategories());
        return savedCoupon;
    }


    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        return couponRepository.findById(id)
                .map(coupon -> {
                    coupon.setCode(updatedCoupon.getCode());
                    coupon.setDiscountType(updatedCoupon.getDiscountType());
                    coupon.setDiscountValue(updatedCoupon.getDiscountValue());
                    coupon.setExpiryDate(updatedCoupon.getExpiryDate());
                    coupon.setUsageLimit(updatedCoupon.getUsageLimit());

                    // Update the categories using the transient categoryIds field.
                    if (updatedCoupon.getCategoryIds() != null && !updatedCoupon.getCategoryIds().isEmpty()) {
                        Set<Category> newCategories = updatedCoupon.getCategoryIds().stream()
                                .map(catId -> categoryRepository.findById(catId)
                                        .orElseThrow(() -> new RuntimeException("Category not found with id " + catId)))
                                .collect(Collectors.toSet());
                        coupon.setCategories(newCategories);
                    } else {
                        coupon.setCategories(Collections.emptySet());
                    }


                    return couponRepository.save(coupon);
                })
                .orElseThrow(() -> new RuntimeException("Coupon not found with id " + id));
    }


        // Additional fields can be updated as needed.


    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }
}

