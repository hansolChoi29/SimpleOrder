package com.sparta.simpleorder.domain.orders.dto.request;

import com.sparta.simpleorder.domain.orders.entity.OrderStatus;


public record UpdateRequestDto(
        int quantity,
        OrderStatus status
) {
}
