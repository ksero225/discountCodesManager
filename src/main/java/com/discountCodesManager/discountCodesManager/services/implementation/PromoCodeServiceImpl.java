package com.discountCodesManager.discountCodesManager.services.implementation;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.repositories.PromoCodeRepository;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeServiceImpl(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    public PromoCodeEntity save(PromoCodeEntity promoCodeEntity) {
        return promoCodeRepository.save(promoCodeEntity);
    }

    @Override
    public Optional<PromoCodeEntity> findOne(String promoCode) {
        return promoCodeRepository.findPromoCodeEntityByPromoCode(promoCode);
    }

    @Override
    public PromoCodeEntity updatePromoCode(String promoCode, PromoCodeEntity promoCodeEntity) {
        Optional<PromoCodeEntity> foundPromoCode = promoCodeRepository.findPromoCodeEntityByPromoCode(promoCode);

        return foundPromoCode.map(existingPromoCode -> {
            Optional.ofNullable(promoCodeEntity.getPromoCodeId()).ifPresent(existingPromoCode::setPromoCodeId);
            Optional.ofNullable(promoCodeEntity.getPromoCode()).ifPresent(existingPromoCode::setPromoCode);
            Optional.ofNullable(promoCodeEntity.getPromoCodeExpirationDate()).ifPresent(existingPromoCode::setPromoCodeExpirationDate);
            Optional.ofNullable(promoCodeEntity.getPromoCodeCurrency()).ifPresent(existingPromoCode::setPromoCodeCurrency);
            Optional.ofNullable(promoCodeEntity.getPromoCodeDiscountAmount()).ifPresent(existingPromoCode::setPromoCodeDiscountAmount);
            Optional.ofNullable(promoCodeEntity.getPromoCodeAllowedUsagesNumber()).ifPresent(existingPromoCode::setPromoCodeAllowedUsagesNumber);

            return promoCodeRepository.save(existingPromoCode);
        }).orElseThrow(() ->
            new RuntimeException("Promo code does not exist")
        );
    }

    @Override
    public void deleteById(String promoCode) {
        promoCodeRepository.deletePromoCodeEntityByPromoCode(promoCode);
    }

    @Override
    public Page<PromoCodeEntity> findAll(Pageable pageable) {
        return promoCodeRepository.findAll(pageable);
    }

    @Override
    public boolean existByPromoCode(String promoCode) {
        return promoCodeRepository.existsByPromoCode(promoCode);
    }
}
