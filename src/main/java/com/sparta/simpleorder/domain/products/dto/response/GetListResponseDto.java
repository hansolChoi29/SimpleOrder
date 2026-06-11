package com.sparta.simpleorder.domain.products.dto.response;

import java.math.BigDecimal;

public record GetListResponseDto(
        Long id,
        String name,
        BigDecimal price,
        int stockQuantity
) {
}
