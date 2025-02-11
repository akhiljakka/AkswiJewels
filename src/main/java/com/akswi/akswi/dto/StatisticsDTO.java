package com.akswi.akswi.dto;

import java.math.BigDecimal;

public class StatisticsDTO {
    private Long totalOrders;
    private BigDecimal totalSales;
    private BigDecimal averageOrderValue;
    private UserSalesDTO topUser;       // The customer with the highest total spent
    private ProductSalesDTO topProduct; // The product with the highest quantity sold

    // Getters and setters
    public Long getTotalOrders() {
        return totalOrders;
    }
    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }
    public BigDecimal getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }
    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }
    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }
    public UserSalesDTO getTopUser() {
        return topUser;
    }
    public void setTopUser(UserSalesDTO topUser) {
        this.topUser = topUser;
    }
    public ProductSalesDTO getTopProduct() {
        return topProduct;
    }
    public void setTopProduct(ProductSalesDTO topProduct) {
        this.topProduct = topProduct;
    }
}