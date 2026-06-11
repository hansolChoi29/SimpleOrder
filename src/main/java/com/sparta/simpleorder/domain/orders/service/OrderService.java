package com.sparta.simpleorder.domain.orders.service;


import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.entity.Order;
import com.sparta.simpleorder.domain.orders.repository.OrderRepository;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.entity.Product;
import com.sparta.simpleorder.domain.products.entity.ProductStatus;
import com.sparta.simpleorder.domain.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateResponseDto create(CreateRequestDto request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (product.getStatus() != ProductStatus.POSSIBLE) {
            throw new IllegalArgumentException("주문 불가능한 상품입니다.");
        }

        Order order = Order.create(
                product,
                request.quantity()
        );
        Order saveOrder = orderRepository.save(order);
        return new CreateResponseDto(saveOrder.getId());
    }
}
