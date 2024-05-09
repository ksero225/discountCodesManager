package com.discountCodesManager.discountCodesManager.controllers;

import com.discountCodesManager.discountCodesManager.domain.dto.ProductDto;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
public class ProductControllers {

    private final ProductService productService;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    public ProductControllers(ProductService productService, Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    //FIXME: ADD VALIDATION FOR PASSED CODES
    //FIXME: ADD VALIDATION FOR PRODUCTS (for example if passed price is not below 0)
    @PostMapping(path = "/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductEntity productEntity = productMapper.mapFrom(productDto);

        if (productEntity.getProductPromoCodes().isEmpty()){
            productEntity.setProductPromoCodes(new HashSet<>()); //Create empty set if not passed in JSON
        }

        ProductEntity savedProductEntity = productService.save(productEntity);

        return new ResponseEntity<>(
                productMapper.mapTo(savedProductEntity),
                HttpStatus.CREATED
        );
    }


}
