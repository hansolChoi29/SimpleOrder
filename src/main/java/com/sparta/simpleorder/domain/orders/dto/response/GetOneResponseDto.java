package com.sparta.simpleorder.domain.orders.dto.response;

import com.sparta.simpleorder.domain.orders.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GetOneResponseDto(
        Long id,
        Long productId,
        String productName,
        int quantity,
        BigDecimal totalPrice,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
