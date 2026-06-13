package com.sparta.simpleorder.domain.products.repository;

import com.sparta.simpleorder.domain.products.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                    SELECT p FROM Product p WHERE p.id = :id
            """)
    Optional<Product> findByIdWithLock(@Param("id") Long id);
}
