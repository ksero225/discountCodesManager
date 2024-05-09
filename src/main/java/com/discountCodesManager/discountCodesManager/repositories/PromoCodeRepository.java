package com.discountCodesManager.discountCodesManager.repositories;

import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeRepository extends JpaRepository<PromoCodeEntity, String> {
}
