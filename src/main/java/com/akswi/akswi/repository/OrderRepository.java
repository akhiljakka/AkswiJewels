package com.akswi.akswi.repository;

import com.akswi.akswi.entity.Order;
import com.akswi.akswi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByOrderDateAfter(LocalDateTime date);

    @Query("SELECT new com.akswi.akswi.dto.UserSalesDTO(o.user.username, SUM(o.finalAmount)) " +
            "FROM Order o GROUP BY o.user.username ORDER BY SUM(o.finalAmount) DESC")
    List<com.akswi.akswi.dto.UserSalesDTO> findTopUserSales(Pageable pageable);
}
