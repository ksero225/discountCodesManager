package com.discountCodesManager.discountCodesManager.mappers.implementations;

import com.discountCodesManager.discountCodesManager.domain.dto.PromoCodeDto;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PromoCodeMapper implements Mapper<PromoCodeEntity, PromoCodeDto> {
    private final ModelMapper modelMapper;

    public PromoCodeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PromoCodeDto mapTo(PromoCodeEntity promoCodeEntity) {
        return modelMapper.map(promoCodeEntity, PromoCodeDto.class);
    }

    @Override
    public PromoCodeEntity mapFrom(PromoCodeDto promoCodeDto) {
        return modelMapper.map(promoCodeDto, PromoCodeEntity.class);
    }
}
