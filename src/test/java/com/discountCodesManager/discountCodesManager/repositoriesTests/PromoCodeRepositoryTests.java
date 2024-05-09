package com.discountCodesManager.discountCodesManager.repositoriesTests;

import com.discountCodesManager.discountCodesManager.repositories.PromoCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    }
}
