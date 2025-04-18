package com.akswi.akswi.service;

import com.akswi.akswi.entity.Category;
import com.akswi.akswi.entity.Product;
import com.akswi.akswi.entity.ProductStatus;
import com.akswi.akswi.repository.CategoryRepository;
import com.akswi.akswi.repository.ProductRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    /** Fetch paginated + filtered products **/
    public Page<Product> getProducts(
            Long categoryId,
            String name,
            ProductStatus status,
            int page,
            int size
    ) {
        return productRepository.findByFilters(
                categoryId, name, status, PageRequest.of(page, size)
        );
    }

    /** Full list, no paging **/
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product saveProduct(Product product) {
        // default timestamps handled by Hibernate
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /** Update every field (including new ones) **/
    public Product updateProduct(Long id, Product updated) {
        return productRepository.findById(id)
                .map(p -> {
                    // basic fields
                    p.setName(updated.getName());
                    p.setDescription(updated.getDescription());
                    p.setImageUrl(updated.getImageUrl());
                    p.setSku(updated.getSku());
                    p.setPrice(updated.getPrice());

                    // new SEO / identity
                    p.setSlug(updated.getSlug());
                    p.setShortDescription(updated.getShortDescription());
                    p.setBenefits(updated.getBenefits());

                    // new pricing
                    p.setCostPrice(updated.getCostPrice());
                    p.setDiscountPrice(updated.getDiscountPrice());
                    p.setLowPrice(updated.getLowPrice());
                    p.setCurrency(updated.getCurrency());

                    // inventory
                    p.setStockQty(updated.getStockQty());
                    p.setStockStatus(updated.getStockStatus());
                    p.setCartLimit(updated.getCartLimit());

                    // flags / status
                    p.setStatus(updated.getStatus());
                    p.setFeatured(updated.getFeatured());
                    p.setNewArrival(updated.getNewArrival());
                    p.setBestSeller(updated.getBestSeller());
                    p.setHotDeal(updated.getHotDeal());

                    // collections
                    p.setTags(updated.getTags());
                    p.setKeywords(updated.getKeywords());
                    p.setCities(updated.getCities());
                    p.setImageUrls(updated.getImageUrls());

                    // SEO metadata
                    p.setMetaTitle(updated.getMetaTitle());
                    p.setMetaDescription(updated.getMetaDescription());
                    p.setMetaKeywords(updated.getMetaKeywords());

                    // category
                    if (updated.getCategory() != null && updated.getCategory().getId() != null) {
                        Category c = categoryRepository.findById(updated.getCategory().getId())
                                .orElseThrow(() -> new RuntimeException("Category not found"));
                        p.setCategory(c);
                    } else {
                        p.setCategory(null);
                    }

                    return productRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /** Bulk import from CSV */
    public Map<String,Object> importProducts(MultipartFile file) {
        int created=0, updated=0;
        try (CSVReader rdr = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = rdr.readNext();
            String[] row;
            while ((row = rdr.readNext()) != null) {
                if (row.length < 5) continue;
                String sku = row[0].trim();
                String name = row[1].trim();
                BigDecimal price = new BigDecimal(row[2].trim());
                String desc = row[3].trim();
                String categoryName = row[4].trim();
                String imageUrl = row.length>5 ? row[5].trim() : "";

                Category cat = resolveCategory(categoryName);

                Product prod = productRepository.findBySku(sku);
                if (prod != null) {
                    prod.setName(name);
                    prod.setPrice(price);
                    prod.setDescription(desc);
                    prod.setCategory(cat);
                    prod.setImageUrl(imageUrl);
                    updated++;
                } else {
                    prod = new Product();
                    prod.setSku(sku);
                    prod.setName(name);
                    prod.setPrice(price);
                    prod.setDescription(desc);
                    prod.setCategory(cat);
                    prod.setImageUrl(imageUrl);
                    created++;
                }
                productRepository.save(prod);
            }
        } catch (IOException|CsvValidationException e) {
            throw new RuntimeException("Failed CSV import: "+e.getMessage());
        }
        Map<String,Object> result = new HashMap<>();
        result.put("created", created);
        result.put("updated", updated);
        result.put("message", "Import finished");
        return result;
    }

    /** Change category on an existing product **/
    public Product updateProductCategory(Long id, Long categoryId) {
        Product p = getProductById(id);
        Category c = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        p.setCategory(c);
        return productRepository.save(p);
    }

    /** Simple category lookup **/
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndStatus(categoryId, null);
    }

    private Category resolveCategory(String name) {
        if (name == null || name.isBlank()) return null;
        Category c = categoryRepository.findByName(name);
        if (c == null) {
            c = new Category();
            c.setName(name);
            c = categoryRepository.save(c);
        }
        return c;
    }
}
