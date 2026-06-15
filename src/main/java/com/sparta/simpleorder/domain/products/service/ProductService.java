package com.sparta.simpleorder.domain.products.service;


import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.request.UpdateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.products.entity.Product;
import com.sparta.simpleorder.domain.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public CreateResponseDto create(
            CreateRequestDto request
    ) {
        validateDuplicateProductName(request.name());

        Product product = Product.create(
                request.name(),
                request.price(),
                request.stockQuantity()
        );
        Product saveProduct = productRepository.save(product);
        return new CreateResponseDto(saveProduct.getId());
    }

    @Transactional(readOnly = true)
    public GetOneResponseDto getOne(Long id) {
        Product product = notFoundProduct(id);

        return new GetOneResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

    @Transactional(readOnly = true)
    public List<GetListResponseDto> getList() {
        return productRepository.findAll().stream().map(
                product -> new GetListResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStockQuantity()
                )
        ).toList();
    }

    @Transactional
    public UpdateResponseDto update(
            UpdateRequestDto request,
            Long id
    ) {
        Product product = notFoundProduct(id);

        if (!product.getName().equals(request.name())) {
            if (productRepository.existsByName(request.name())) {
                throw new IllegalArgumentException("이미 존재하는 상품입니다.");
            }
        }
        product.update(
                request.name(),
                request.price(),
                request.stockQuantity(),
                request.status()
        );

        return new UpdateResponseDto(product.getId());
    }

    @Transactional
    public void delete(Long id) {
        Product product = notFoundProduct(id);

        product.isDelete();
    }

    private Product notFoundProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private void validateDuplicateProductName(String name) {
        if (productRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 상품입니다.");
        }
    }
}
