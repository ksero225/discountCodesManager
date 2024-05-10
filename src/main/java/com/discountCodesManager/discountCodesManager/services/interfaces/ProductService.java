package com.discountCodesManager.discountCodesManager.services.interfaces;

import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    ProductEntity save(ProductEntity productEntity);

    boolean doesProductExists(Long productId);

    Optional<ProductEntity> findOne(Long productId);

    Page<ProductEntity> findAll(Pageable pageable);

    ProductEntity partialUpdate(Long productId,ProductEntity productEntity);

    void deleteById(Long productId);
}
