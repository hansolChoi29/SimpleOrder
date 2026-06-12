package com.sparta.simpleorder.domain.products.service;

import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.request.UpdateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.products.entity.Product;
import com.sparta.simpleorder.domain.products.entity.ProductStatus;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
    @DisplayName("상품생성_이름중복")
    void create_duplicate() {
        CreateRequestDto request = new CreateRequestDto(
                "name",
                new BigDecimal(1000),
                1
        );
        given(productRepository.existsByName("name")).willReturn(true);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 상품입니다.");
        verify(productRepository).existsByName("name");
        verify(productRepository, never()).save(any(Product.class));
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

        GetOneResponseDto response = productService.getOne(productId);
        verify(productRepository).findById(productId);
        assertThat(response.id()).isEqualTo(productId);
    }

    @Test
    @DisplayName("상품단건조회_존재하지않은_상품")
    void getOne_find_notFound() {
        Long productId = 1L;

        given(productRepository.findById(productId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getOne(productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");

        verify(productRepository).findById(productId);
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

        List<GetListResponseDto> response = productService.getList();
        verify(productRepository).findAll();
        assertThat(response.get(0).id()).isEqualTo(productId);
    }

    @Test
    @DisplayName("상품수정_성공")
    void update() {
        Long productId = 1L;
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        ReflectionTestUtils.setField(product, "id", productId);
        UpdateRequestDto request = new UpdateRequestDto(
                "rename",
                new BigDecimal(1000),
                1,
                ProductStatus.IMPOSSIBLE
        );

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(productRepository.existsByName("rename")).willReturn(false);

        UpdateResponseDto response = productService.update(request, productId);
        assertThat(response.id()).isEqualTo(productId);

        verify(productRepository).findById(anyLong());
        verify(productRepository).existsByName("rename");
    }

    @Test
    @DisplayName("상품수정_존재하지않는_상품")
    void update_notFound() {
        Long productId = 1L;
        UpdateRequestDto request = new UpdateRequestDto(
                "name",
                new BigDecimal(1000),
                1,
                ProductStatus.IMPOSSIBLE
        );
        assertThatThrownBy(() -> productService.update(request, productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("상품수정_이름_중복")
    void update_duplicate() {
        Long productId = 1L;
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        UpdateRequestDto request = new UpdateRequestDto(
                "rename",
                new BigDecimal(1000),
                1,
                ProductStatus.IMPOSSIBLE
        );
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.existsByName("rename")).willReturn(true);

        assertThatThrownBy(() -> productService.update(request, productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 상품입니다.");

        verify(productRepository).findById(productId);
        verify(productRepository).existsByName("rename");
    }

    @Test
    @DisplayName("상품삭제_성공")
    void isDelete() {
        Long productId = 1L;

        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        productService.delete(productId);

        assertThat(product.getStatus()).isEqualTo(ProductStatus.DELETED);
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("상품삭제_존재하지않는_상품")
    void delete_notFound() {
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 상품입니다.");
        verify(productRepository).findById(productId);
    }
}
