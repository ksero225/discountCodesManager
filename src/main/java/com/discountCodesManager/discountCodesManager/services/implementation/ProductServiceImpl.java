package com.discountCodesManager.discountCodesManager.services.implementation;

import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.repositories.ProductRepository;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public ProductEntity save(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public Optional<ProductEntity> findOne(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Optional<ProductEntity> findOneByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    @Override
    public Page<ProductEntity> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public ProductEntity partialUpdate(Long productId, ProductEntity productEntity) {
        Optional<ProductEntity> foundProduct = productRepository.findById(productId);

        return foundProduct.map(existingProduct ->{
            Optional.ofNullable(productEntity.getProductName()).ifPresent(existingProduct::setProductName);
            Optional.ofNullable(productEntity.getProductDescription()).ifPresent(existingProduct::setProductDescription);
            Optional.ofNullable(productEntity.getProductCurrency()).ifPresent(existingProduct::setProductCurrency);
            Optional.ofNullable(productEntity.getProductPrice()).ifPresent(existingProduct::setProductPrice);

            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product does not exist"));
    }

    @Override
    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }

//    @Override
//    public boolean doesProductExists(Long productId) {
//        return productRepository.existsById(productId);
//    }
}
