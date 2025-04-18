package com.akswi.akswi.controller;

import com.akswi.akswi.dto.StatisticsDTO;
import com.akswi.akswi.entity.Order;
import com.akswi.akswi.entity.User;
import com.akswi.akswi.service.OrderService;
import com.akswi.akswi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin()
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService  orderService;

    @Autowired
    private UserService userService;

    // Create a new order for the authenticated user
    @PostMapping
    public Order createOrder(@RequestBody Order order, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        return orderService.createOrder(order);
    }

    // Get order history for the authenticated user
    @GetMapping("/my")
    public List<Order> getMyOrders(Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderService.getOrdersByUser(user);
    }

    @GetMapping
    public List<Order> getOrders(){
        return orderService.getOrders();
    }

    @PutMapping("/{orderId}")
    public Order updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> statusUpdate) {
        String newStatus = statusUpdate.get("status");
        return orderService.updateOrderStatus(orderId, newStatus);
    }

    @GetMapping("/{orderId}")
    public Optional<Order> getOrderById(@PathVariable Long orderId){
        return orderService.getOrderById(orderId);
    }
    @GetMapping("/statistics")
    public StatisticsDTO getStatistics(@RequestParam(defaultValue = "3days") String range) {
        return orderService.getStatistics(range);
    }


}

