package com.akswi.akswi.dto;

public class ProductSalesDTO {
    private String productName;
    private Long totalQuantitySold;

    public ProductSalesDTO(String productName, Long totalQuantitySold) {
        this.productName = productName;
        this.totalQuantitySold = totalQuantitySold;
    }

    public String getProductName() {
        return productName;
    }

    public Long getTotalQuantitySold() {
        return totalQuantitySold;
    }
}
