package com.sparta.simpleorder.domain.orders.dto.request;


public record CreateRequestDto(
        Long productId,
        int quantity
) {
}
