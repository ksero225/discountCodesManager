package com.discountCodesManager.discountCodesManager.controllersTests;

import com.discountCodesManager.discountCodesManager.TestDataUtilities;
import com.discountCodesManager.discountCodesManager.domain.dto.PromoCodeDto;
import com.discountCodesManager.discountCodesManager.domain.entities.PromoCodeEntity;
import com.discountCodesManager.discountCodesManager.services.interfaces.PromoCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PromoCodeControllerTests {
    private final PromoCodeService promoCodeService;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public PromoCodeControllerTests(PromoCodeService promoCodeService, ObjectMapper objectMapper, MockMvc mockMvc) {
        this.promoCodeService = promoCodeService;
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreatePromoCodeSuccessfullyReturnsHttpStatus201Created() throws Exception {
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();

        String testPromoCodeEntityJson = objectMapper.writeValueAsString(testPromoCodeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/promoCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeEntityJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatePromoCodeReturnsHttpStatus409ConflictWhenPromoCodeIsInvalid() throws Exception {
        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();
        testPromoCodeDto.setPromoCode("1");
        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/promoCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatCreatePromoCodeReturnsHttpStatus409ConflictWhenPromoCodeAlreadyExist() throws Exception {
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();

        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/promoCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatCreatePromoCodeReturnsHttpStatus409ConflictWhenPromoExpirationDateIsWrong() throws Exception {
        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();
        LocalDate wrongDate = LocalDate.of(2023, 1, 1);
        testPromoCodeDto.setPromoCodeExpirationDate(wrongDate);
        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/promoCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    public void testThatCreatePromoCodeSuccessfullyReturnsPromoCode() throws Exception {
        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();

        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/promoCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCode").value("11111")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeExpirationDate").value("2025-01-01")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeCurrency").value("EUR")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeDiscountAmount").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeAllowedUsagesNumber").value(100)
        );
    }

    @Test
    public void testThatGetOnePromoCodeSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/promoCode/" + testPromoCodeEntity.getPromoCode())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetOnePromoCodeReturnsHttpStatus404NotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/promoCode/999")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatListPromoCodeReturnsPromoCode() throws Exception {
        PromoCodeEntity testPromoCodeEntityA = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntityA);
        PromoCodeEntity testPromoCodeEntityB = TestDataUtilities.createTestPromoCodeEntityB();
        promoCodeService.save(testPromoCodeEntityB);
        PromoCodeEntity testPromoCodeEntityC = TestDataUtilities.createTestPromoCodeEntityC();
        promoCodeService.save(testPromoCodeEntityC);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/promoCode")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].promoCodeId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].promoCode").value("11111")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].promoCodeExpirationDate").value("2025-01-01")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].promoCodeCurrency").value("EUR")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].promoCodeDiscountAmount").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].promoCodeAllowedUsagesNumber").value(100)
        );
    }

    @Test
    public void testThatFullUpdatePromoCodeReturnsHttpStatus404NotFound() throws Exception {
        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();
        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/promoCode/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdatePromoCodeReturnsUpdatedPromoCode() throws Exception {
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();
        testPromoCodeDto.setPromoCode("UPDATED");
        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/promoCode/" + testPromoCodeEntity.getPromoCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCode").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeExpirationDate").value("2025-01-01")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeCurrency").value("EUR")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeDiscountAmount").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeAllowedUsagesNumber").value(100)
        );
    }

    @Test
    public void testThatPartialUpdatePromoCodeReturnsHttpStatus404NotFound() throws Exception {
        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();
        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/promoCode/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdatePromoCodeReturnsUpdatedPromoCode() throws Exception {
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        PromoCodeDto testPromoCodeDto = TestDataUtilities.createTestPromoCodeDtoA();
        testPromoCodeDto.setPromoCode("UPDATED");
        String testPromoCodeDtoJson = objectMapper.writeValueAsString(testPromoCodeDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/promoCode/" + testPromoCodeEntity.getPromoCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPromoCodeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCode").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeExpirationDate").value("2025-01-01")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeCurrency").value("EUR")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeDiscountAmount").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.promoCodeAllowedUsagesNumber").value(100)
        );
    }

    @Test
    public void testThatDeleteProductReturnsHttpStatus204NoContent() throws Exception {
        PromoCodeEntity testPromoCodeEntity = TestDataUtilities.createTestPromoCodeEntityA();
        promoCodeService.save(testPromoCodeEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/promoCode/" + testPromoCodeEntity.getPromoCode())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
