package com.akswi.akswi.service;

import com.akswi.akswi.entity.Coupon;
import com.akswi.akswi.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setCode(updatedCoupon.getCode());
        coupon.setDiscountType(updatedCoupon.getDiscountType());
        coupon.setDiscountValue(updatedCoupon.getDiscountValue());
        coupon.setExpiryDate(updatedCoupon.getExpiryDate());
        coupon.setUsageLimit(updatedCoupon.getUsageLimit());
        // Additional fields can be updated as needed.
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }
}

