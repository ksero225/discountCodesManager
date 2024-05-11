package com.discountCodesManager.discountCodesManager.domain.dto;

import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PromoCodeDto {
    private Long promoCodeId;
    private String promoCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate promoCodeExpirationDate;

    private String promoCodeCurrency;
    private BigDecimal promoCodeDiscountAmount;

    private Integer promoCodeAllowedUsagesNumber;

}
