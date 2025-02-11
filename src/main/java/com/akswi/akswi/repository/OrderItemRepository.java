package com.akswi.akswi.repository;

import com.akswi.akswi.dto.ProductSalesDTO;
import com.akswi.akswi.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT new com.akswi.akswi.dto.ProductSalesDTO(oi.product.name, SUM(oi.quantity)) " +
            "FROM OrderItem oi GROUP BY oi.product.name ORDER BY SUM(oi.quantity) DESC")
    List<ProductSalesDTO> findTopProductSales(Pageable pageable);
}
