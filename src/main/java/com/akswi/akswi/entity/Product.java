package com.akswi.akswi.entity;



import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long productId;

        @Column(nullable = false, length = 100)
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;
    ///
        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;


        // Add the sku field that the repository query expects
        @Column(unique = true, nullable = false)
        private String sku;
    ///

        @Column(nullable = false)
        private BigDecimal price;

        private Integer stock = 0;

        @Column(name = "image_url", length = 1000)
        private String imageUrl;

        @CreationTimestamp
        private LocalDateTime createdAt;

        // Getters and setters
        // … (omitted for brevity)

        public Long getProductId() {
            return productId;
        }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
///
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    ///
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
