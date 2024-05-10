package com.discountCodesManager.discountCodesManager.repositoriesTests;

import com.discountCodesManager.discountCodesManager.TestDataUtilities;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.repositories.PromoCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PromoCodeRepositoryTests {
    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public PromoCodeRepositoryTests(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Test
    public void testThatPromoCodeCanBeCreatedAndRecalled() {
        PromoCodeEntity promoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeRepository.save(promoCodeEntity);

        Optional<PromoCodeEntity> result = promoCodeRepository.findById(promoCodeEntity.getPromoCodeId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(promoCodeEntity);
    }

    @Test
    public void testThatMultiplePromoCodesCanBeCreatedAndRecalled() {
        PromoCodeEntity promoCodeEntityA = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeRepository.save(promoCodeEntityA);

        PromoCodeEntity promoCodeEntityB = TestDataUtilities.createTestPromoCodeEntityB();
        promoCodeRepository.save(promoCodeEntityB);

        PromoCodeEntity promoCodeEntityC = TestDataUtilities.createTestPromoCodeEntityC();
        promoCodeRepository.save(promoCodeEntityC);

        Iterable<PromoCodeEntity> result = promoCodeRepository.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        promoCodeEntityA,
                        promoCodeEntityB,
                        promoCodeEntityC
                );
    }

    @Test
    public void testThatPromoCodeCanBeUpdatedAndRecalled(){
        PromoCodeEntity promoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeRepository.save(promoCodeEntity);

        Optional<PromoCodeEntity> result = promoCodeRepository.findById(promoCodeEntity.getPromoCodeId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(promoCodeEntity);

        promoCodeEntity.setPromoCodeCurrency("UPDATED");

        promoCodeRepository.save(promoCodeEntity);

        result = promoCodeRepository.findById(promoCodeEntity.getPromoCodeId());

        assertThat(result).isPresent();
        assertThat(result.get().getPromoCodeCurrency()).isEqualTo("UPDATED");
    }

    @Test
    public void testThatPromoCodeCanBeDeleted(){
        PromoCodeEntity promoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeRepository.save(promoCodeEntity);

        Optional<PromoCodeEntity> result = promoCodeRepository.findById(promoCodeEntity.getPromoCodeId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(promoCodeEntity);

        promoCodeRepository.deleteById(promoCodeEntity.getPromoCodeId());
        result = promoCodeRepository.findById(promoCodeEntity.getPromoCodeId());

        assertThat(result).isEmpty();
    }
}
