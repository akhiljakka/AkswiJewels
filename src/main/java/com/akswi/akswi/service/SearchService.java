package com.akswi.akswi.service;

import com.akswi.akswi.entity.Product;
import com.akswi.akswi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> searchProducts(String query) {
        // If query is empty or null, return all products or an empty list
        if (query == null || query.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}
