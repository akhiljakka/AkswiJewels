package com.akswi.akswi.repository;

import com.akswi.akswi.entity.Order;
import com.akswi.akswi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
