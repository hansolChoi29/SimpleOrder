package com.sparta.simpleorder.domain.products.service;


import com.sparta.simpleorder.domain.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
 private final ProductRepository productRepository;
}
