package com.discountCodesManager.discountCodesManager.controllers;

import com.discountCodesManager.discountCodesManager.domain.dto.PurchaseDto;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.domain.entities.PurchaseEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import com.discountCodesManager.discountCodesManager.services.interfaces.PurchaseService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PromoCodeService promoCodeService;
    private final ProductService productService;
    private final Mapper<PurchaseEntity, PurchaseDto> purchaseMapper;
    private final LocalDate today = LocalDate.now();

    public PurchaseController(
            PurchaseService purchaseService,
            PromoCodeService promoCodeService,
            ProductService productService,
            Mapper<PurchaseEntity, PurchaseDto> purchaseMapper
    ) {
        this.purchaseService = purchaseService;
        this.promoCodeService = promoCodeService;
        this.productService = productService;
        this.purchaseMapper = purchaseMapper;
    }

    @GetMapping(path = "/discount/{productId}")
    public ResponseEntity<String> getDiscountPrice(
            @PathVariable("productId") Long productNameId,
            @RequestParam("promoCode") String promoCode
    ) {

        Optional<ProductEntity> foundProduct = productService.findOne(productNameId);
        Optional<PromoCodeEntity> foundPromoCode = promoCodeService.findOne(promoCode);

        if (foundPromoCode.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promo code does not exist");
        } else if (foundProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist");
        }

        ProductInformation productInformation = getProductInformation(foundProduct.get(), foundPromoCode.get());

        return ResponseEntity.ok(productInformation.getMessage());
    }

    @GetMapping(path = "/purchase/{productId}")
    public ResponseEntity<PurchaseDto> purchaseProduct(
            @PathVariable("productId") Long productId,
            @RequestParam("promoCode") String promoCode
    ) {

        Optional<ProductEntity> foundProduct = productService.findOne(productId);
        Optional<PromoCodeEntity> foundPromoCode = promoCodeService.findOne(promoCode);

        if (foundPromoCode.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promo code does not exist");
        } else if (foundProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist");
        }

        PurchaseEntity purchaseEntity = getPurchaseEntity(foundPromoCode.get(), foundProduct.get());

        PurchaseEntity savedPurchaseEntity = purchaseService.save(purchaseEntity);

        Integer promoCodeUsages = foundPromoCode.get().getPromoCodeAllowedUsagesNumber();
        foundPromoCode.get().setPromoCodeAllowedUsagesNumber(promoCodeUsages - 1);
        promoCodeService.updatePromoCode(
                promoCode,
                foundPromoCode.get()
        );

        return new ResponseEntity<>(
                purchaseMapper.mapTo(savedPurchaseEntity),
                HttpStatus.CREATED
        );
    }


    private PurchaseEntity getPurchaseEntity(
            PromoCodeEntity foundPromoCode,
            ProductEntity foundProduct
    ) {
        ProductInformation productInformation = getProductInformation(foundProduct, foundPromoCode);

        return PurchaseEntity
                .builder()
                .purchaseDate(today)
                .purchaseProductBasicPrice(foundPromoCode.getPromoCodeDiscountAmount())
                .purchaseDiscountApplied(productInformation.getProductPrice())
                .product(foundProduct)
                .build();
    }

    @Getter
    private class ProductInformation {
        private final String message;
        private final BigDecimal productPrice;

        public ProductInformation(String message, BigDecimal productPrice) {
            this.message = message;
            this.productPrice = productPrice;
        }
    }

    private ProductInformation getProductInformation(ProductEntity productEntity, PromoCodeEntity promoCodeEntity) {

        BigDecimal basePrice = productEntity.getProductPrice();
        String productCurrency = productEntity.getProductCurrency();
        String promoCodeCurrency = promoCodeEntity.getPromoCodeCurrency();

        final Integer promoCodeUsages = promoCodeEntity.getPromoCodeAllowedUsagesNumber();
        final String productName = productEntity.getProductName();
        final String promoCode = promoCodeEntity.getPromoCode();
        String message;

        if (promoCodeEntity.getPromoCodeExpirationDate().isBefore(today)) {
            message = String.format("Promo code is expired, base price is %s %s.",
                    basePrice,
                    productCurrency
            );

            return new ProductInformation(message, basePrice);
        }
        if (!productCurrency.equals(promoCodeCurrency)) {
            message = String.format("Promo code currency (%s) does not match product currency (%s), base price is %s %s.",
                    promoCodeCurrency,
                    productCurrency,
                    basePrice,
                    productCurrency
            );
            return new ProductInformation(message, basePrice);
        }
        if (promoCodeUsages <= 0) {
            message = String.format("The maximum usage limit has been reached for this promo code, base price is %s %s.",
                    basePrice,
                    productCurrency
            );
            return new ProductInformation(message, basePrice);
        }

        final BigDecimal promoCodeDiscountAmount = promoCodeEntity.getPromoCodeDiscountAmount();
        final BigDecimal discountedPrice = basePrice.subtract(promoCodeDiscountAmount);

        if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0) {
            message = String.format("Price of %s with a promo code %s is 0 %s",
                    productName,
                    promoCode,
                    productCurrency
            );

            return new ProductInformation(message, BigDecimal.ZERO);
        } else {
            message = String.format("Price of %s with a promo code %s is %s %s (original price %s)",
                    productName,
                    promoCode,
                    discountedPrice,
                    productCurrency,
                    basePrice
            );
            return new ProductInformation(message, discountedPrice);
        }
    }
}
