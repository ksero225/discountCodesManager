package com.discountCodesManager.discountCodesManager.domain.dto;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductDto {
    private Long productId;

    private String productName;
    private String productDescription;

    private BigDecimal productPrice;
    private String productCurrency;
    private Set<PromoCodeEntity> productPromoCodes = new HashSet<>();
}
