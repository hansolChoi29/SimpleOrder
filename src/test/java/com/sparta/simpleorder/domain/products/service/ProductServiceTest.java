package com.sparta.simpleorder.domain.products.service;

import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetListResponse;
import com.sparta.simpleorder.domain.products.dto.response.GetOneResponse;
import com.sparta.simpleorder.domain.products.entity.Product;
import com.sparta.simpleorder.domain.products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("상품생성_성공")
    void create() {
        String name = "name";
        CreateRequestDto request = new CreateRequestDto(
                "name",
                new BigDecimal(10000),
                1
        );
        Product productSave = Product.create(
                "name",
                new BigDecimal(10000),
                1
        );

        given(productRepository.existsByName(name)).willReturn(false);
        given(productRepository.save(any(Product.class))).willReturn(productSave);

        CreateResponseDto response = productService.create(request);

        verify(productRepository).existsByName(name);
        verify(productRepository).save(any(Product.class));
        assertThat(response.id()).isEqualTo(productSave.getId());
    }

    @Test
    @DisplayName("상품단건조회_성공")
    void getOne() {
        Long productId = 1L;

        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        ReflectionTestUtils.setField(product, "id", productId);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        GetOneResponse response = productService.getOne(productId);
        verify(productRepository).findById(productId);
        assertThat(response.id()).isEqualTo(productId);
    }

    @Test
    @DisplayName("상품전체조회_성공")
    void getList() {
        Long productId = 1L;
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findAll()).willReturn(List.of(product));

        List<GetListResponse> response = productService.getList();
        verify(productRepository).findAll();
        assertThat(response.get(0).id()).isEqualTo(productId);
    }
}