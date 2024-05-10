package com.discountCodesManager.discountCodesManager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

}
