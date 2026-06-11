package com.sparta.simpleorder.domain.products.dto.request;

import java.math.BigDecimal;

public record UpdateRequest (
        String name,
        BigDecimal price,
        int stockQuantity
){
}
