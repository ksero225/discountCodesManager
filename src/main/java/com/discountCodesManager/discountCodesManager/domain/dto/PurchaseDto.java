package com.discountCodesManager.discountCodesManager.domain.dto;

import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {
    private Long purchaseId;

    private LocalDate purchaseDate;
    private BigDecimal purchaseProductBasicPrice;
    private BigDecimal purchaseDiscountApplied;
    private BigDecimal purchaseFinalPrice;

    private ProductEntity product;
}
