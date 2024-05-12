package com.discountCodesManager.discountCodesManager.services.implementation;

import com.discountCodesManager.discountCodesManager.domain.entities.PurchaseEntity;
import com.discountCodesManager.discountCodesManager.repositories.ProductRepository;
import com.discountCodesManager.discountCodesManager.repositories.PromoCodeRepository;
import com.discountCodesManager.discountCodesManager.repositories.PurchaseRepository;
import com.discountCodesManager.discountCodesManager.services.interfaces.PurchaseService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final PurchaseRepository purchaseRepository;;

    public PurchaseServiceImpl(ProductRepository productRepository, PromoCodeRepository promoCodeRepository, PurchaseRepository purchaseRepository) {
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public PurchaseEntity save(PurchaseEntity purchaseEntity) {
        return purchaseRepository.save(purchaseEntity);
    }
}
