package com.akswi.akswi.repository;

import com.akswi.akswi.entity.Product;
import com.akswi.akswi.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findBySku(String sku);

    List<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status);

    List<Product> findByNameContainingIgnoreCase(String name);

    // Pagination + status
    Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

    Page<Product> findByCategoryIdAndNameContainingIgnoreCaseAndStatus(
            Long categoryId, String name, ProductStatus status, Pageable pageable);

    @Query("""
      SELECT p
      FROM Product p
      WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
        AND (:status     IS NULL OR p.status  = :status)
        AND (:name       IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      """)
    Page<Product> findByFilters(Long categoryId, String name, ProductStatus status, Pageable pageable);
}
