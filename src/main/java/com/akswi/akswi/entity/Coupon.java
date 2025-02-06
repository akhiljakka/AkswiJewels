package com.akswi.akswi.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // "percentage" or "fixed"
    @Column(nullable = false, length = 20)
    private String discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    private LocalDate expiryDate;

    private Integer usageLimit = 1;

    private Integer usedCount = 0;

    // Optional association if the coupon is for a specific user
    @ManyToOne
    @JoinColumn(name = "user_specific")
    private User userSpecific;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Getters and setters
    // … (omitted for brevity)

    public Long getCouponId() {
        return couponId;
    }
    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    public BigDecimal getDiscountValue() {
        return discountValue;
    }
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Integer getUsageLimit() {
        return usageLimit;
    }
    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }
    public Integer getUsedCount() {
        return usedCount;
    }
    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }
    public User getUserSpecific() {
        return userSpecific;
    }
    public void setUserSpecific(User userSpecific) {
        this.userSpecific = userSpecific;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
