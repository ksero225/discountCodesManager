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
    private String promoCodeId; // 3-24 alphanumeric case-sensitive characters with no whitespaces

    private LocalDate promoCodeExpirationDate;

    private String promoCodeCurrency;
    private BigDecimal promoCodeDiscountAmount;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity promoCodeProduct;
}
