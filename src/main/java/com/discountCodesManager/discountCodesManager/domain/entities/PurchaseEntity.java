package com.discountCodesManager.discountCodesManager.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "purchases_history")
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseId;

    private LocalDateTime purchaseDate;
    private BigDecimal purchaseDiscountApplied;

    private Long productId;
}
