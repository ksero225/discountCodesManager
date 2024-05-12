package com.discountCodesManager.discountCodesManager.repositories;

import com.discountCodesManager.discountCodesManager.domain.entities.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {
}
