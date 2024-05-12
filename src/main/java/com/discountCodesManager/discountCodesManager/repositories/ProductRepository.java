package com.discountCodesManager.discountCodesManager.repositories;

import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, PagingAndSortingRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductName (String productName);
}
