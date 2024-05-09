package com.discountCodesManager.discountCodesManager.services.implementation;

import com.discountCodesManager.discountCodesManager.repositories.ProductRepository;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


}