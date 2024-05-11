package com.discountCodesManager.discountCodesManager.repositories;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCodeEntity, Long>, PagingAndSortingRepository<PromoCodeEntity, Long> {
    boolean existsByPromoCode(String promoCode);
    Optional<PromoCodeEntity> findPromoCodeEntityByPromoCode(String promoCode);
    void deletePromoCodeEntityByPromoCode (String promoCode);
}
