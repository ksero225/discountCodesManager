package com.discountCodesManager.discountCodesManager.mappers.implementations;

import com.discountCodesManager.discountCodesManager.domain.dto.PurchaseDto;
import com.discountCodesManager.discountCodesManager.domain.entities.PurchaseEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper implements Mapper<PurchaseEntity, PurchaseDto> {
    private final ModelMapper modelMapper;

    public PurchaseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PurchaseDto mapTo(PurchaseEntity purchaseEntity) {
        return modelMapper.map(purchaseEntity, PurchaseDto.class);
    }

    @Override
    public PurchaseEntity mapFrom(PurchaseDto purchaseDto) {
        return modelMapper.map(purchaseDto, PurchaseEntity.class);
    }
}
