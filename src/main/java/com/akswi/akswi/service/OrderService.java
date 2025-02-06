package com.akswi.akswi.service;

import com.akswi.akswi.entity.Coupon;
import com.akswi.akswi.entity.Order;
import com.akswi.akswi.entity.Product;
import com.akswi.akswi.entity.User;
import com.akswi.akswi.repository.CouponRepository;
import com.akswi.akswi.repository.OrderRepository;
import com.akswi.akswi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(Order order) {
        // Validate and apply coupon if provided
        if (order.getCoupon() != null) {
            Coupon coupon = couponRepository.findById(order.getCoupon().getCouponId())
                    .orElseThrow(() -> new RuntimeException("Invalid coupon"));
            // Check expiry and usage limits
            if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Coupon expired");
            }
            if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
                throw new RuntimeException("Coupon usage limit exceeded");
            }
            BigDecimal discount = BigDecimal.ZERO;
            if ("percentage".equalsIgnoreCase(coupon.getDiscountType())) {
                discount = order.getTotalAmount()
                        .multiply(coupon.getDiscountValue())
                        .divide(new BigDecimal("100"));
            } else if ("fixed".equalsIgnoreCase(coupon.getDiscountType())) {
                discount = coupon.getDiscountValue();
            }
            order.setDiscountAmount(discount);
            order.setFinalAmount(order.getTotalAmount().subtract(discount));
            // Update coupon usage count
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.save(coupon);
        } else {
            order.setDiscountAmount(BigDecimal.ZERO);
            order.setFinalAmount(order.getTotalAmount());
        }

        // Process each order item (e.g., update stock)
        order.getOrderItems().forEach(item -> {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
            item.setOrder(order);
        });

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}

