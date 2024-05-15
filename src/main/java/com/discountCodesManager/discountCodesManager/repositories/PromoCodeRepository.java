package com.discountCodesManager.discountCodesManager.repositories;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCodeEntity, Long>, PagingAndSortingRepository<PromoCodeEntity, Long> {
    boolean existsByPromoCode(String promoCode);
    Optional<PromoCodeEntity> findPromoCodeEntityByPromoCode(String promoCode);

    @Transactional
    void deletePromoCodeEntityByPromoCode (String promoCode);
}
