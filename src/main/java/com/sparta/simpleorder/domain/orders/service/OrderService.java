package com.sparta.simpleorder.domain.orders.service;


import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.request.UpdateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.orders.entity.Order;
import com.sparta.simpleorder.domain.orders.repository.OrderRepository;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.entity.Product;
import com.sparta.simpleorder.domain.products.entity.ProductStatus;
import com.sparta.simpleorder.domain.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateResponseDto create(CreateRequestDto request) {
        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        if (product.getStatus() != ProductStatus.POSSIBLE) {
            throw new IllegalArgumentException("주문 불가능한 상품입니다.");
        }
        product.decreaseStock(request.quantity());

        Order order = Order.create(
                product,
                request.quantity()
        );
        Order saveOrder = orderRepository.save(order);
        return new CreateResponseDto(saveOrder.getId());
    }

    @Transactional(readOnly = true)
    public GetOneResponseDto getOne(Long id) {
        Order order = orderNotFound(id);
        return new GetOneResponseDto(
                order.getId(),
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GetListResponseDto> getList(){
        return orderRepository.findAll()
                .stream()
                .map(
                        order -> new GetListResponseDto(
                                order.getId(),
                                order.getProduct().getId(),
                                order.getProduct().getName(),
                                order.getQuantity(),
                                order.getTotalPrice(),
                                order.getStatus(),
                                order.getCreatedAt()
                        )
                )
                .toList();
    }

    @Transactional
    public UpdateResponseDto update(
            UpdateRequestDto request,
            Long id
    ){
        Order order = orderNotFound(id);
        order.update(
                request.quantity(),
                request.status()
        );
        return new UpdateResponseDto(order.getId());
    }

    @Transactional
    public void delete(Long id){
        Order order = orderNotFound(id);
        order.isDelete();
    }

    private Order orderNotFound(Long id){
        return orderRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 주문내역입니다."));
    }
}
