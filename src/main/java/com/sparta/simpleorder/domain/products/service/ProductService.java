package com.sparta.simpleorder.domain.products.service;


import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.entity.Product;
import com.sparta.simpleorder.domain.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public CreateResponseDto create(
            CreateRequestDto request
    ) {
        if (productRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 상품입니다.");
        }
        Product product = Product.create(
                request.name(),
                request.price(),
                request.stockQuantity()
        );
        Product saveProduct = productRepository.save(product);
        return new CreateResponseDto(saveProduct.getId());
    }
}
