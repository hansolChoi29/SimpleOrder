package com.sparta.simpleorder.domain.products.repository;

import com.sparta.simpleorder.domain.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
