package com.discountCodesManager.discountCodesManager;

import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

public class TestDataUtilities {

    public static ProductEntity createTestProductEntityA() {
        return ProductEntity.builder()
                .productId(1L)
                .productName("Keyboard")
                .productDescription("Plank with keys")
                .productPrice(BigDecimal.valueOf(11.11))
                .productCurrency("EUR")
                .productPromoCodes(new HashSet<>())
                .build();
    }

    public static ProductEntity createTestProductEntityB() {
        return ProductEntity.builder()
                .productId(2L)
                .productName("Mouse")
                .productDescription("Gaming mouse")
                .productPrice(BigDecimal.valueOf(22.22))
                .productCurrency("PLN")
                .productPromoCodes(new HashSet<>())
                .build();
    }

    public static ProductEntity createTestProductEntityC() {
        return ProductEntity.builder()
                .productId(3L)
                .productName("Lamp")
                .productDescription("It shines")
                .productPrice(BigDecimal.valueOf(33.33))
                .productCurrency("USD")
                .productPromoCodes(new HashSet<>())
                .build();
    }

    public static PromoCodeEntity createTestPromoCodeEntityA(){
        LocalDate promoCodeExpirationDate = LocalDate.of(2025, 1,1);

        return PromoCodeEntity.builder()
            .promoCodeId("promoCodeNumber1")
                .promoCodeExpirationDate(promoCodeExpirationDate)
                .promoCodeCurrency("EUR")
                .promoCodeDiscountAmount(BigDecimal.valueOf(11.11))
                .promoCodeProduct(null)
                .build();
    }

    public static PromoCodeEntity createTestPromoCodeEntityB(){
        LocalDate promoCodeExpirationDate = LocalDate.of(2025, 2,2);

        return PromoCodeEntity.builder()
                .promoCodeId("promoCodeNumber2")
                .promoCodeExpirationDate(promoCodeExpirationDate)
                .promoCodeCurrency("PLN")
                .promoCodeDiscountAmount(BigDecimal.valueOf(22.22))
                .promoCodeProduct(null)
                .build();
    }

    public static PromoCodeEntity createTestPromoCodeEntityC(){
        LocalDate promoCodeExpirationDate = LocalDate.of(2025, 3,3);

        return PromoCodeEntity.builder()
                .promoCodeId("promoCodeNumber3")
                .promoCodeExpirationDate(promoCodeExpirationDate)
                .promoCodeCurrency("USD")
                .promoCodeDiscountAmount(BigDecimal.valueOf(33.33))
                .promoCodeProduct(null)
                .build();
    }
}
