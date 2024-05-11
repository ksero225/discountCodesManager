package com.discountCodesManager.discountCodesManager.services.interfaces;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromoCodeService {
    PromoCodeEntity save(PromoCodeEntity promoCodeEntity);

    Page<PromoCodeEntity> findAll(Pageable pageable);

    PromoCodeEntity updatePromoCode(String promoCodeId, PromoCodeEntity promoCodeEntity);

    void deleteById(String promoCodeId);

    boolean existByPromoCode(String promoCode);

    Optional<PromoCodeEntity> findOne(String promoCode);
}
