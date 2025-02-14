package com.akswi.akswi.controller;

import com.akswi.akswi.entity.Coupon;
import com.akswi.akswi.entity.Order;
import com.akswi.akswi.entity.Product;
import com.akswi.akswi.service.CouponService;
import com.akswi.akswi.service.OrderService;
import com.akswi.akswi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderService orderService;

    // Product Management
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

//    @PutMapping("/products/{id}")
//    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
//        Product product = productService.getProductById(id);
//        product.setName(updatedProduct.getName());
//        product.setDescription(updatedProduct.getDescription());
//        product.setPrice(updatedProduct.getPrice());
//        product.setStock(updatedProduct.getStock());
//        product.setImageUrl(updatedProduct.getImageUrl());
//        return productService.saveProduct(product);
//    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // Coupon Management
    @GetMapping("/coupons")
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @PostMapping("/coupons")
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return couponService.createCoupon(coupon);
    }

    @PutMapping("/coupons/{id}")
    public Coupon updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
        return couponService.updateCoupon(id, coupon);
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().build();
    }

    // Update Order Status (Admin)
    @PutMapping("/orders/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
}
