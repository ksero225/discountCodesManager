package com.discountCodesManager.discountCodesManager.controllers;

import com.discountCodesManager.discountCodesManager.domain.dto.ProductDto;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ProductControllers {

    private final ProductService productService;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    public ProductControllers(ProductService productService, Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }


    //TODO: ADD VALIDATION FOR PRODUCTS (for example if passed price is below 0)
    @PostMapping(path = "/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductEntity productEntity = productMapper.mapFrom(productDto);

        ProductEntity savedProductEntity = productService.save(productEntity);

        return new ResponseEntity<>(
                productMapper.mapTo(savedProductEntity),
                HttpStatus.CREATED
        );
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<ProductDto> getOneProductById(@PathVariable("productId") Long productId) {
        Optional<ProductEntity> foundProduct = productService.findOne(productId);

        return foundProduct.map(ProductEntity -> {
            ProductDto productDto = productMapper.mapTo(ProductEntity);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping(path = "/product")
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<ProductEntity> allFoundProducts = productService.findAll(pageable);

        return allFoundProducts.map(productMapper::mapTo);
    }

    //TODO: Validation as well

    @PutMapping(path = "/product/{productId}")
    public ResponseEntity<ProductDto> fullUpdateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductDto productDto
    ) {

        checkProductExistence(productId);
        productDto.setProductId(productId);
        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity savedProductEntity = productService.save(productEntity);

        return new ResponseEntity<>(productMapper.mapTo(savedProductEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/product/{productId}")
    public ResponseEntity<ProductDto> partialUpdateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductDto productDto
    ) {
        checkProductExistence(productId);

        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity savedProductEntity = productService.partialUpdate(productId, productEntity);

        return new ResponseEntity<>(productMapper.mapTo(savedProductEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        checkProductExistence(productId);

        productService.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void checkProductExistence(Long productId) {
        if (productService.findOne(productId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }
    }
}
