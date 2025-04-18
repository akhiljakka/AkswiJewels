package com.akswi.akswi.entity;

import com.akswi.akswi.entity.Category;
import com.akswi.akswi.entity.ProductStatus;
import com.akswi.akswi.entity.StockStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    // ─── your existing fields ──────────────────────────────────────────────
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private BigDecimal price;


    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
    // ─── end of your existing fields ────────────────────────────────────────

    // ─── NEW FIELDS ─────────────────────────────────────────────────────────

    // slug for SEO / URLs
    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    // track updates
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // a brief blurb for list pages
    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    // additional “benefits” block
    @Column(columnDefinition = "TEXT")
    private String benefits;

    // cost & discount pricing
    @Column(name = "cost_price", precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "discount_price", precision = 12, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "low_price", precision = 12, scale = 2)
    private BigDecimal lowPrice;

    // currency code (e.g. USD, EUR)
    @Column(nullable = false, length = 3)
    private String currency = "USD";

    // actual quantity vs. your old `stock`
    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", nullable = false, length = 20)
    private StockStatus stockStatus = StockStatus.in_stock;

    // limit per cart
    @Column(name = "cart_limit", nullable = false)
    private Integer cartLimit = 5;

    // active/inactive/etc
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.active;

    // simple flags for UI highlighting
    private Boolean isFeatured   = false;
    private Boolean isNewArrival = false;
    private Boolean isBestSeller = false;
    private Boolean isHotDeal    = false;

    // tags, keywords, cities, images as collections
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag", length = 50)
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "product_keywords", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(name = "product_cities", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "city")
    private List<String> cities;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> imageUrls;

    // simple analytics fields
    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;
    private Integer reviewCount = 0;
    private Integer salesCount  = 0;
    private Integer viewCount   = 0;

    // SEO metadata
    @Column(length = 255)
    private String metaTitle;

    @Column(columnDefinition = "TEXT")
    private String metaDescription;

    @ElementCollection
    @CollectionTable(name = "product_meta_keywords", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "meta_keyword")
    private List<String> metaKeywords;
    // ─── end NEW FIELDS ─────────────────────────────────────────────────────

    // … existing getters/setters …

    // add getters/setters for your new fields below…

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Integer getCartLimit() {
        return cartLimit;
    }

    public void setCartLimit(Integer cartLimit) {
        this.cartLimit = cartLimit;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Boolean getFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public Boolean getNewArrival() {
        return isNewArrival;
    }

    public void setNewArrival(Boolean newArrival) {
        isNewArrival = newArrival;
    }

    public Boolean getBestSeller() {
        return isBestSeller;
    }

    public void setBestSeller(Boolean bestSeller) {
        isBestSeller = bestSeller;
    }

    public Boolean getHotDeal() {
        return isHotDeal;
    }

    public void setHotDeal(Boolean hotDeal) {
        isHotDeal = hotDeal;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public List<String> getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(List<String> metaKeywords) {
        this.metaKeywords = metaKeywords;
    }
}
