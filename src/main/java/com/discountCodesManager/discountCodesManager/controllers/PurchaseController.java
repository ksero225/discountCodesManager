package com.discountCodesManager.discountCodesManager.controllers;

import com.discountCodesManager.discountCodesManager.domain.dto.PurchaseDto;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.domain.entities.PurchaseEntity;
import com.discountCodesManager.discountCodesManager.mappers.Mapper;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import com.discountCodesManager.discountCodesManager.services.interfaces.PurchaseService;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping(path = "/discount/{productId}/{promoCode}")
    public ResponseEntity<String> getDiscountPrice(
            @PathVariable("productId") Long productNameId,
            @PathVariable("promoCode") String promoCode
    ) {

        Optional<ProductEntity> foundProduct = productService.findOne(productNameId);
        Optional<PromoCodeEntity> foundPromoCode = promoCodeService.findOne(promoCode);

        if (foundPromoCode.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promo code does not exist");
        } else if (foundProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist");
        }

        ProductInformation productInformation = new ProductInformation(foundProduct.get(), foundPromoCode.get());
        productInformation.setProductInformation();

        return ResponseEntity.ok(productInformation.getMessage());
    }

    @GetMapping(path = "/purchase/{productId}/{promoCode}")
    public ResponseEntity<PurchaseDto> purchaseProduct(
            @PathVariable("productId") Long productId,
            @PathVariable("promoCode") String promoCode
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
        promoCodeService.updatePromoCode(promoCode, foundPromoCode.get());

        return new ResponseEntity<>(purchaseMapper.mapTo(savedPurchaseEntity), HttpStatus.CREATED);
    }

    private PurchaseEntity getPurchaseEntity(PromoCodeEntity foundPromoCode, ProductEntity foundProduct) {

        ProductInformation productInformation = new ProductInformation(foundProduct, foundPromoCode);
        productInformation.setProductInformation();

        PurchaseEntity purchaseEntity = new PurchaseEntity();

        purchaseEntity.setPurchaseDate(today);
        purchaseEntity.setPurchaseProductBasicPrice(foundPromoCode.getPromoCodeDiscountAmount());
        purchaseEntity.setPurchaseDiscountApplied(productInformation.getProductPrice());
        purchaseEntity.setProduct(foundProduct);
        return purchaseEntity;
    }

    @Data
    private class ProductInformation {
        private ProductEntity productEntity;
        private PromoCodeEntity promoCodeEntity;
        @Getter
        private String message;
        @Getter
        private BigDecimal productPrice;

        public ProductInformation(ProductEntity productEntity, PromoCodeEntity promoCodeEntity) {
            this.productEntity = productEntity;
            this.promoCodeEntity = promoCodeEntity;
        }

        public void setProductInformation() {
            BigDecimal basePrice = productEntity.getProductPrice();
            String productCurrency = productEntity.getProductCurrency();
            String promoCodeCurrency = promoCodeEntity.getPromoCodeCurrency();

            final Integer promoCodeUsages = promoCodeEntity.getPromoCodeAllowedUsagesNumber();
            final String productName = productEntity.getProductName();
            final String promoCode = promoCodeEntity.getPromoCode();

            final BigDecimal promoCodeDiscountAmount = promoCodeEntity.getPromoCodeDiscountAmount();
            final BigDecimal discountedPrice = basePrice.subtract(promoCodeDiscountAmount);

            if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0) {
                this.message = String.format("Price of %s with a promo code %s is 0 %s",
                        productName,
                        promoCode,
                        productCurrency
                );

                this.productPrice = BigDecimal.ZERO;
            } else {
                this.message = String.format("Price of %s with a promo code %s is %s %s (original price %s)",
                        productName,
                        promoCode,
                        discountedPrice,
                        productCurrency,
                        basePrice
                );

                this.productPrice = discountedPrice;
            }

            if (promoCodeEntity.getPromoCodeExpirationDate().isBefore(today)) {
                this.message = String.format("Promo code is expired, base price is %s %s.",
                        basePrice,
                        productCurrency
                );
                this.productPrice = basePrice;
            }

            if (!productCurrency.equals(promoCodeCurrency)) {
                this.message = String.format("Promo code currency (%s) does not match product currency (%s), base price is %s %s.",
                        promoCodeCurrency,
                        productCurrency,
                        basePrice,
                        productCurrency
                );

                this.productPrice = basePrice;
            }

            if (promoCodeUsages <= 0) {
                this.message = String.format("The maximum usage limit has been reached for this promo code, base price is %s %s.",
                        basePrice,
                        productCurrency
                );

                this.productPrice = basePrice;
            }

        }
    }

//    private BigDecimal getPurchasePrice(ProductEntity productEntity, PromoCodeEntity promoCodeEntity) {
//
//        BigDecimal basePrice = productEntity.getProductPrice();
//        String productCurrency = productEntity.getProductCurrency();
//        String promoCodeCurrency = promoCodeEntity.getPromoCodeCurrency();
//
//        final Integer promoCodeUsages = promoCodeEntity.getPromoCodeAllowedUsagesNumber();
//
//
//        if (promoCodeEntity.getPromoCodeExpirationDate().isBefore(today)) {
//            return basePrice;
//        } else if (!productCurrency.equals(promoCodeCurrency)) {
//            return basePrice;
//        } else if (promoCodeUsages <= 0) {
//            return basePrice;
//        } else {
//            final BigDecimal promoCodeDiscountAmount = promoCodeEntity.getPromoCodeDiscountAmount();
//            final BigDecimal discountedPrice = basePrice.subtract(promoCodeDiscountAmount);
//
//            if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0) {
//                return BigDecimal.ZERO;
//            } else {
//                return discountedPrice;
//            }
//        }
//    }
//
//
//    private String generateDiscountMessage(ProductEntity productEntity, PromoCodeEntity promoCodeEntity) {
//        LocalDate today = LocalDate.now();
//
//        BigDecimal basePrice = productEntity.getProductPrice();
//        String productCurrency = productEntity.getProductCurrency();
//        String promoCodeCurrency = promoCodeEntity.getPromoCodeCurrency();
//
//        final Integer promoCodeUsages = promoCodeEntity.getPromoCodeAllowedUsagesNumber();
//        final String productName = productEntity.getProductName();
//        final String promoCode = promoCodeEntity.getPromoCode();
//
//        String message = "";
//
//        if (promoCodeEntity.getPromoCodeExpirationDate().isBefore(today)) {
//            message = String.format("Promo code is expired, base price is %s %s.",
//                    basePrice,
//                    productCurrency
//            );
//            return message;
//        } else if (!productCurrency.equals(promoCodeCurrency)) {
//            message = String.format("Promo code currency (%s) does not match product currency (%s), base price is %s %s.",
//                    promoCodeCurrency,
//                    productCurrency,
//                    basePrice,
//                    productCurrency
//            );
//            return message;
//        } else if (promoCodeUsages <= 0) {
//            message = String.format("The maximum usage limit has been reached for this promo code, base price is %s %s.",
//                    basePrice,
//                    productCurrency
//            );
//            return message;
//        } else {
//            final BigDecimal promoCodeDiscountAmount = promoCodeEntity.getPromoCodeDiscountAmount();
//            final BigDecimal discountedPrice = basePrice.subtract(promoCodeDiscountAmount);
//
//            if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0) {
//                message = String.format("Price of %s with a promo code %s is 0 %s",
//                        productName,
//                        promoCode,
//                        productCurrency
//                );
//
//                return message;
//            } else {
//                message = String.format("Price of %s with a promo code %s is %s %s (original price %s)",
//                        productName,
//                        promoCode,
//                        discountedPrice,
//                        productCurrency,
//                        basePrice
//                );
//
//            }
//        }
//
//        return message;
//    }
}
