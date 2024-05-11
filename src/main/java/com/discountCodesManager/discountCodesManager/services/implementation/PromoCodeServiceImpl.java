package com.discountCodesManager.discountCodesManager.services.implementation;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.repositories.PromoCodeRepository;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    public Optional<PromoCodeEntity> findOne(Long promoCodeId) {
        return promoCodeRepository.findById(promoCodeId);
    }

    @Override
    public PromoCodeEntity partialUpdate(Long promoCodeId, PromoCodeEntity promoCodeEntity) {
        Optional<PromoCodeEntity> foundPromoCode = promoCodeRepository.findById(promoCodeId);

        return foundPromoCode.map(existingPromoCode -> {
            Optional.ofNullable(promoCodeEntity.getPromoCodeId()).ifPresent(existingPromoCode::setPromoCodeId);
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
    public void deleteById(Long promoCodeId) {
        promoCodeRepository.deleteById(promoCodeId);
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
