package com.akswi.akswi.service;

import com.akswi.akswi.entity.Product;
import com.akswi.akswi.entity.Category;
import com.akswi.akswi.repository.ProductRepository;
import com.akswi.akswi.repository.CategoryRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock());
                    product.setImageUrl(updatedProduct.getImageUrl());
                    product.setSku(updatedProduct.getSku());

                    // Update the category explicitly:
                    if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {
                        Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                                .orElseThrow(() -> new RuntimeException("Category not found with id " + updatedProduct.getCategory().getId()));
                        product.setCategory(category);
                    } else {
                        product.setCategory(null);
                    }

                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    // New method: Import products from a CSV file.
    // Expected CSV header: sku,name,price,description,categoryName
    public Map<String, Object> importProducts(MultipartFile file) {
        int createdCount = 0;
        int updatedCount = 0;
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            // Read header row: sku, name, price, description, categoryName
            String[] header = reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 4) continue; // Ensure required fields are present.

                String sku = line[0].trim();
                String name = line[1].trim();
                String priceStr = line[2].trim();
                String description = line[3].trim();
                String categoryName = (line.length > 4) ? line[4].trim() : null;

                if (sku.isEmpty() || name.isEmpty() || priceStr.isEmpty()) continue;

                // Convert price string to BigDecimal.
                BigDecimal price = new BigDecimal(priceStr);

                // Resolve or create the category based on categoryName.
                Category category = null;
                if (categoryName != null && !categoryName.isEmpty()) {
                    category = categoryRepository.findByName(categoryName);
                    if (category == null) {
                        category = new Category();
                        category.setName(categoryName);
                        category.setDescription("");
                        category = categoryRepository.save(category);
                    }
                }

                // Check if a product with the given SKU already exists.
                Product product = productRepository.findBySku(sku);
                if (product != null) {
                    // Update the existing product.
                    product.setName(name);
                    product.setPrice(price);
                    product.setDescription(description);
                    product.setCategory(category);
                    productRepository.save(product);
                    updatedCount++;
                } else {
                    // Create a new product.
                    Product newProduct = new Product();
                    newProduct.setSku(sku);
                    newProduct.setName(name);
                    newProduct.setPrice(price);
                    newProduct.setDescription(description);
                    newProduct.setCategory(category);
                    productRepository.save(newProduct);
                    createdCount++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to import products: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException("Failed to import products: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("created", createdCount);
        result.put("updated", updatedCount);
        result.put("message", "Import completed successfully");
        return result;
    }

    // New method: Update the category of an existing product by its ID.
    public Product updateProductCategory(Long id, Long categoryId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
        product.setCategory(category);
        return productRepository.save(product);
    }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

}
