package com.sparta.simpleorder.domain.products.dto.response;

import java.math.BigDecimal;

public record GetListResponse(
        Long id,
        String name,
        BigDecimal price,
        int stockQuantity
) {
}
