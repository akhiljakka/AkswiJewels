package com.akswi.akswi.dto;

import java.math.BigDecimal;

public class UserSalesDTO {
    private String username;
    private BigDecimal totalSpent;

    public UserSalesDTO(String username, BigDecimal totalSpent) {
        this.username = username;
        this.totalSpent = totalSpent;
    }

    public String getUsername() {
        return username;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
}
