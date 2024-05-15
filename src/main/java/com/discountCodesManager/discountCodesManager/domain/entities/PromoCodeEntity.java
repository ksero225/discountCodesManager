package com.discountCodesManager.discountCodesManager.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promo_codes")
public class PromoCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promoCodeId;// 3-24 alphanumeric case-sensitive characters with no whitespaces

    private String promoCode;

    private LocalDate promoCodeExpirationDate;

    private String promoCodeCurrency;
    private BigDecimal promoCodeDiscountAmount;

    private Integer promoCodeAllowedUsagesNumber;

}
