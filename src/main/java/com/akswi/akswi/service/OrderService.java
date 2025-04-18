package com.akswi.akswi.service;

import com.akswi.akswi.dto.ProductSalesDTO;
import com.akswi.akswi.dto.StatisticsDTO;
import com.akswi.akswi.dto.UserSalesDTO;
import com.akswi.akswi.entity.*;
import com.akswi.akswi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository; // Newly added to update user points

    @Transactional
    public Order createOrder(Order order) {
        // Validate and apply coupon if provided
        if (order.getCoupon() != null) {
            Coupon coupon = couponRepository.findById(order.getCoupon().getCouponId())
                    .orElseThrow(() -> new RuntimeException("Invalid coupon"));
            // Check expiry and usage limits
            if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now().toLocalDate())) {
                throw new RuntimeException("Coupon expired");
            }
            if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
                throw new RuntimeException("Coupon usage limit exceeded");
            }
            if (order.getDeliveryAddress() != null && order.getDeliveryAddress().getAddressId() != null) {
                // Fetch the complete Address from the database
                Address address = addressRepository.findById(order.getDeliveryAddress().getAddressId())
                        .orElseThrow(() -> new RuntimeException("Address not found"));
                order.setDeliveryAddress(address);
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

        // Process each order item (update product stock)
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            // Check stock (optionally, throw exception if insufficient)
            if(item.getQuantity() > product.getStockQty()){
                throw new RuntimeException("Insufficient stock for product: " + product.getName() +
                        ". Only " + product.getStockQty() + " available.");
            }
            // Deduct ordered quantity from product stock
            product.setStockQty(product.getStockQty() - item.getQuantity());
            productRepository.save(product);
            item.setOrder(order);
            // Optionally, set price at order time
            item.setPrice(product.getPrice());
        }

        // Save the order first
        Order savedOrder = orderRepository.save(order);

        // Award points to the user equal to the final amount of the order
        User user = savedOrder.getUser();
        if(user.getPoints() == null){
            user.setPoints(BigDecimal.ZERO);
        }
        user.setPoints(user.getPoints().add(savedOrder.getFinalAmount()));
        userRepository.save(user); // Update user with new points

        return savedOrder;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }



    public StatisticsDTO getStatistics(String range) {
        LocalDateTime startDate;
        switch (range.toLowerCase()) {
            case "3days":
                startDate = LocalDateTime.now().minusDays(3);
                break;
            case "3months":
                startDate = LocalDateTime.now().minusMonths(3);
                break;
            case "6months":
                startDate = LocalDateTime.now().minusMonths(6);
                break;
            default:
                startDate = LocalDateTime.MIN;
        }

        List<Order> orders = orderRepository.findByOrderDateAfter(startDate);
        Long totalOrders = (long) orders.size();
        BigDecimal totalSales = orders.stream()
                .map(Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageOrderValue = totalOrders > 0 ?
                totalSales.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Pageable topOne = PageRequest.of(0, 1);
        List<UserSalesDTO> topUserList = orderRepository.findTopUserSales(topOne);
        UserSalesDTO topUser = topUserList.isEmpty() ? null : topUserList.get(0);
        List<ProductSalesDTO> topProductList = orderItemRepository.findTopProductSales(topOne);
        ProductSalesDTO topProduct = topProductList.isEmpty() ? null : topProductList.get(0);

        StatisticsDTO stats = new StatisticsDTO();
        stats.setTotalOrders(totalOrders);
        stats.setTotalSales(totalSales);
        stats.setAverageOrderValue(averageOrderValue);
        stats.setTopUser(topUser);
        stats.setTopProduct(topProduct);

        return stats;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }


    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
