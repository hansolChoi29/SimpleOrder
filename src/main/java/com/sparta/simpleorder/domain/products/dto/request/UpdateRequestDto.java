package com.sparta.simpleorder.domain.products.dto.request;

import com.sparta.simpleorder.domain.products.entity.ProductStatus;

import java.math.BigDecimal;

public record UpdateRequestDto(
        String name,
        BigDecimal price,
        int stockQuantity,
        ProductStatus status
){
}
