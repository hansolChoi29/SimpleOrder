package com.sparta.simpleorder.domain.products.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "products")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    public static Product create(
            String name,
            BigDecimal price,
            int stockQuantity
    ) {
        Product product = new Product();
        product.name = name;
        product.price = price;
        product.stockQuantity = stockQuantity;
        product.status = ProductStatus.POSSIBLE;
        return product;
    }

    public void update(
            String name,
            BigDecimal price,
            int stockQuantity,
            ProductStatus status
    ) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
    }

    public void isDelete() {
        this.status = ProductStatus.DELETED;
    }

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }
}
