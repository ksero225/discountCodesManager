package com.discountCodesManager.discountCodesManager.mappers.implementations;

import com.discountCodesManager.discountCodesManager.domain.dto.ProductDto;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<ProductDto, ProductEntity> {

    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductEntity mapTo(ProductDto productDto) {
        return modelMapper.map(productDto, ProductEntity.class);
    }

    @Override
    public ProductDto mapFrom(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDto.class);
    }
}
