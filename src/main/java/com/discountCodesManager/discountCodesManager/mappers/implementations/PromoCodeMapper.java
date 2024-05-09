package com.discountCodesManager.discountCodesManager.mappers.implementations;

import com.discountCodesManager.discountCodesManager.domain.dto.PromoCodeDto;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PromoCodeMapper implements Mapper<PromoCodeDto, PromoCodeEntity> {
    private final ModelMapper modelMapper;

    public PromoCodeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public PromoCodeEntity mapTo(PromoCodeDto promoCodeDto) {
        return modelMapper.map(promoCodeDto, PromoCodeEntity.class);
    }

    @Override
    public PromoCodeDto mapFrom(PromoCodeEntity promoCodeEntity) {
        return modelMapper.map(promoCodeEntity, PromoCodeDto.class);
    }
}
