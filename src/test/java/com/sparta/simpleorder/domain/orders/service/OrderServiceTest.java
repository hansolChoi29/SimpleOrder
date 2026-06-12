package com.sparta.simpleorder.domain.orders.service;

import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.request.UpdateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.orders.entity.Order;
import com.sparta.simpleorder.domain.orders.entity.OrderStatus;
import com.sparta.simpleorder.domain.orders.repository.OrderRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName("주문생성_성공")
    void create() {
        Long productId = 1L;
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        CreateRequestDto request = new CreateRequestDto(
                productId,
                1
        );
        Order order = Order.create(
                product,
                1
        );
        ReflectionTestUtils.setField(order, "id", 1L);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        CreateResponseDto response = orderService.create(request);
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));

        assertThat(response.id()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("주문단건조회_성공")
    void getOne() {
        Long orderId = 1L;
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        Order order = Order.create(
                product,
                1
        );
        ReflectionTestUtils.setField(order, "id", orderId);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        GetOneResponseDto response = orderService.getOne(orderId);
        verify(orderRepository).findById(orderId);

        assertThat(response.id()).isEqualTo(order.getId());
        assertThat(response.productId()).isEqualTo(product.getId());
        assertThat(response.productName()).isEqualTo(product.getName());
        assertThat(response.quantity()).isEqualTo(order.getQuantity());
        assertThat(response.totalPrice()).isEqualTo(order.getTotalPrice());
        assertThat(response.status()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    @DisplayName("주문전체조회_성공")
    void getList() {
        Long orderId = 1L;
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        Order order = Order.create(
                product,
                1
        );
        ReflectionTestUtils.setField(order, "id", orderId);

        given(orderRepository.findAll()).willReturn(List.of(order));

        List<GetListResponseDto> response = orderService.getList();
        verify(orderRepository).findAll();
        assertThat(response.get(0).id()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("주문수정_성공")
    void update() {
        Long orderId = 1L;
        UpdateRequestDto request = new UpdateRequestDto(
                1,
                OrderStatus.ORDERED
        );
        Product product = Product.create(
                "name",
                new BigDecimal(1000),
                1
        );
        Order order = Order.create(
                product,
                1
        );
        ReflectionTestUtils.setField(order, "id", 1L);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        UpdateResponseDto response = orderService.update(request, orderId);
        assertThat(response.id()).isEqualTo(orderId);

        verify(orderRepository).findById(orderId);
    }
}