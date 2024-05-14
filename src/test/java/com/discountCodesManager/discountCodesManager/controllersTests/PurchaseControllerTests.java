package com.discountCodesManager.discountCodesManager.controllersTests;

import com.discountCodesManager.discountCodesManager.TestDataUtilities;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import com.discountCodesManager.discountCodesManager.services.interfaces.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PurchaseControllerTests {
    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final PromoCodeService promoCodeService;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public PurchaseControllerTests(PurchaseService purchaseService, ProductService productService, PromoCodeService promoCodeService, ObjectMapper objectMapper, MockMvc mockMvc) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.promoCodeService = promoCodeService;
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatGetDiscountPriceReturnsHttpStatus404NotFoundWhenPromoCodeDoesntExist() throws Exception{
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntity);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/discount/1?promoCode=999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetDiscountPriceReturnsHttpStatus404NotFoundWhenProductDoesntExist() throws Exception{
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discount/5?promoCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatSavePurchaseSuccessfullyReturnsHttpStatus201Created() throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntity);

        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/purchase/1?promoCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatGetDiscountPriceSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntity);

        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/discount/1?promoCode=11111")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


}
