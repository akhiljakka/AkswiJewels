package com.akswi.akswi.controller;

import com.akswi.akswi.entity.Product;
import com.akswi.akswi.entity.ProductStatus;
import com.akswi.akswi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    @Autowired private ProductService productService;

    /** Paginated & filtered **/
    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.getProducts(categoryId, name, status, page, size);
    }

    /** Full list **/
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /** Single product **/
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /** Create **/
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product p) {
        return ResponseEntity.status(201).body(productService.saveProduct(p));
    }

    /** Update **/
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product p
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, p));
    }

    /** Delete **/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /** Bulk CSV import **/
    @PostMapping("/import")
    public ResponseEntity<Map<String,Object>> importProducts(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(productService.importProducts(file));
    }

    /** Update category only **/
    @PutMapping("/{id}/category")
    public ResponseEntity<Product> updateCategory(
            @PathVariable Long id,
            @RequestBody Map<String,Long> body
    ) {
        return ResponseEntity.ok(
                productService.updateProductCategory(id, body.get("categoryId"))
        );
    }

    /** Simple by-category list **/
    @GetMapping("/category")
    public List<Product> byCategory(@RequestParam Long categoryId) {
        return productService.findByCategory(categoryId);
    }
}
