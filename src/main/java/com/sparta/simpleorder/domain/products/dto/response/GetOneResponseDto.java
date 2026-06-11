package com.sparta.simpleorder.domain.products.dto.response;

import java.math.BigDecimal;

public record GetOneResponseDto(
        Long id,
        String name,
        BigDecimal price,
        int stockQuantity
) {
}
