package com.discountCodesManager.discountCodesManager.repositoriesTests;

import com.discountCodesManager.discountCodesManager.TestDataUtilities;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryTests {
    private final ProductRepository productRepository;

    @Autowired
    public ProductRepositoryTests(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
    }

    @Test
    public void testThatProductCanBeCreatedAndRecalled() {
        ProductEntity productEntity = TestDataUtilities.createTestProductEntityA();
        productRepository.save(productEntity);

        Optional<ProductEntity> result = productRepository.findById(productEntity.getProductId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntity);
    }

    @Test
    public void testThatMultipleProductsCanBeCreatedAndRecalled() {
        ProductEntity productEntityA = TestDataUtilities.createTestProductEntityA();
        productRepository.save(productEntityA);

        ProductEntity productEntityB = TestDataUtilities.createTestProductEntityB();
        productRepository.save(productEntityB);

        ProductEntity productEntityC = TestDataUtilities.createTestProductEntityC();
        productRepository.save(productEntityC);

        Iterable<ProductEntity> result = productRepository.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        productEntityA,
                        productEntityB,
                        productEntityC
                );
    }

    @Test
    public void testThatProductCanBeUpdatedAndRecalled() {
        ProductEntity productEntity = TestDataUtilities.createTestProductEntityA();
        productRepository.save(productEntity);

        Optional<ProductEntity> result = productRepository.findById(productEntity.getProductId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntity);

        productEntity.setProductName("UPDATED");

        productRepository.save(productEntity);

        result = productRepository.findById(productEntity.getProductId());

        assertThat(result).isPresent();
        assertThat(result.get().getProductName()).isEqualTo("UPDATED");
    }

    @Test
    public void testThatProductCanBeDeleted() {
        ProductEntity productEntity = TestDataUtilities.createTestProductEntityA();
        productRepository.save(productEntity);

        Optional<ProductEntity> result = productRepository.findById(productEntity.getProductId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntity);

        productRepository.deleteById(productEntity.getProductId());
        result = productRepository.findById(productEntity.getProductId());

        assertThat(result).isEmpty();
    }
}
