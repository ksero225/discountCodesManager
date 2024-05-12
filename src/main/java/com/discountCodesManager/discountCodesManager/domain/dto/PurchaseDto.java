package com.discountCodesManager.discountCodesManager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDto {
    private Long purchaseId;

    private LocalDateTime purchaseDate;
    private BigDecimal purchaseDiscountApplied;

    private Long productId;
}
