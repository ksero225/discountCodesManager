package com.discountCodesManager.discountCodesManager.services.interfaces;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromoCodeService {
    PromoCodeEntity save(PromoCodeEntity promoCodeEntity);

    Optional<PromoCodeEntity> findOne(Long promoCodeId);

    Page<PromoCodeEntity> findAll(Pageable pageable);

    PromoCodeEntity partialUpdate(Long promoCodeId, PromoCodeEntity promoCodeEntity);

    void deleteById(Long promoCodeId);

    boolean existByPromoCode(String promoCode);
}
