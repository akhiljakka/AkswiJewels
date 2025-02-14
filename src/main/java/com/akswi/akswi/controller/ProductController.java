package com.akswi.akswi.controller;

import com.akswi.akswi.entity.Product;
import com.akswi.akswi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin()
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET all products
    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    // GET a product by its ID
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // PUT to update an existing product (all fields)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /////
//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//        Product savedProduct = productService.saveProduct(product);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
//    }
    /////



    // POST endpoint for bulk CSV import of products
    // CSV should include headers such as: sku, name, price, description, categoryName
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importProducts(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = productService.importProducts(file);
        return ResponseEntity.ok(result);
    }

    // PUT endpoint to update the category of an existing product by its ID.
    // Expects a JSON body like: { "categoryId": 123 }
    @PutMapping("/{id}/category")
    public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        Long categoryId = request.get("categoryId");
        Product updatedProduct = productService.updateProductCategory(id, categoryId);
        return ResponseEntity.ok(updatedProduct);
    }
}
