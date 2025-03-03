package com.akswi.akswi.controller;

import com.akswi.akswi.entity.Coupon;
import com.akswi.akswi.entity.Category;
import com.akswi.akswi.repository.CouponRepository;
import com.akswi.akswi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public Coupon createCoupon(@RequestBody CouponRequest couponRequest) {
        Coupon coupon = new Coupon();
        coupon.setCode(couponRequest.getCode());
 //       coupon.setDiscount(BigDecimal.valueOf(couponRequest.getDiscount()));
        coupon.setDiscountType(couponRequest.getDiscountType());
        coupon.setDiscountValue(BigDecimal.valueOf(couponRequest.getDiscountValue()));
        coupon.setStartDate(couponRequest.getStartDate());
        coupon.setEndDate(couponRequest.getEndDate());
        coupon.setExpiryDate(couponRequest.getExpiryDate());
        coupon.setUsageLimit(couponRequest.getUsageLimit());
        coupon.setUsedCount(0);

        // Process the categoryIds
        if (couponRequest.getCategoryIds() != null && !couponRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (Long catId : couponRequest.getCategoryIds()) {
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

    // DTO for coupon creation
    public static class CouponRequest {
        private String code;
        private Double discount;
        private String discountType;
        private Double discountValue;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDate expiryDate;
        private Integer usageLimit;
        private List<Long> categoryIds;
        // Getters and setters...
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Double getDiscount() { return discount; }
        public void setDiscount(Double discount) { this.discount = discount; }
        public String getDiscountType() { return discountType; }
        public void setDiscountType(String discountType) { this.discountType = discountType; }
        public Double getDiscountValue() { return discountValue; }
        public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public LocalDate getExpiryDate() { return expiryDate; }
        public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
        public Integer getUsageLimit() { return usageLimit; }
        public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
        public List<Long> getCategoryIds() { return categoryIds; }
        public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
    }
}
