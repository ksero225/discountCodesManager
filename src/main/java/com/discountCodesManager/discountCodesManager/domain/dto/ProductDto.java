package com.discountCodesManager.discountCodesManager.domain.dto;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long productId;

    private String productName;
    private String productDescription;

    private BigDecimal productPrice;
    private String productCurrency;

    private Set<PromoCodeEntity> productPromoCodes = new HashSet<>();
}
