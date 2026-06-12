package com.sparta.simpleorder.domain.orders.entity;

import com.sparta.simpleorder.domain.products.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static Order create(
            Product product,
            int quantity
    ) {
        Order order = new Order();
        order.product = product;
        order.quantity = quantity;
        order.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        order.status = OrderStatus.ORDERED;
        order.createdAt = LocalDateTime.now();
        return order;
    }

    public void update(
            int quantity,
            OrderStatus status
    ) {
        this.quantity = quantity;
        this.status = status;
    }

    public void isDelete() {
        this.status = OrderStatus.CANCELLED;
    }
}
