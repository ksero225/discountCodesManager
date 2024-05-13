package com.discountCodesManager.discountCodesManager.controllersTests;

import com.discountCodesManager.discountCodesManager.TestDataUtilities;
import com.discountCodesManager.discountCodesManager.domain.dto.ProductDto;
import com.discountCodesManager.discountCodesManager.domain.entities.ProductEntity;
import com.discountCodesManager.discountCodesManager.services.interfaces.ProductService;
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

import java.math.BigDecimal;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTests {
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public ProductControllerTests(ProductService productService, ObjectMapper objectMapper, MockMvc mockMvc) {
        this.productService = productService;
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreateProductSuccessfullyReturnsHttpStatus201Created() throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();

        String testProductEntityJson = objectMapper.writeValueAsString(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductEntityJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateProductSuccessfullyReturnsCreatedProduct() throws Exception{
        ProductDto testProductDto = TestDataUtilities.createTestProductDtoA();

        String testProductDtoJson = objectMapper.writeValueAsString(testProductDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testProductDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productName").value("Keyboard")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productDescription").value("Plank with keys")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productPrice").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCurrency").value("EUR")
        );
    }

    @Test
    public void testThatGetOneProductByIdSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();

        productService.save(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/product/" + testProductEntity.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetOneProductByIdReturnsHttpStatus404NotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/product/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatListProductReturnsProduct() throws Exception {
        ProductEntity testProductEntityA = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntityA);

        ProductEntity testProductEntityB = TestDataUtilities.createTestProductEntityB();
        productService.save(testProductEntityB);

        ProductEntity testProductEntityC = TestDataUtilities.createTestProductEntityC();
        productService.save(testProductEntityC);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/product")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].productId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].productName").value("Keyboard")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].productDescription").value("Plank with keys")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].productPrice").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].productCurrency").value("EUR")
        );

    }

    @Test
    public void testThatFullUpdateProductReturnsUpdatedProduct () throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntity);

        ProductDto testProductDto = TestDataUtilities.createTestProductDtoA();
        testProductDto.setProductName("UPDATED");

        String jsonTestProductDto = objectMapper.writeValueAsString(testProductDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/product/" + testProductDto.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestProductDto)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productName").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productDescription").value("Plank with keys")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productPrice").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCurrency").value("EUR")
        );
    }

    @Test
    public void testThatFullUpdateProductReturnsHttpStatus404NotFound() throws Exception{
        ProductDto testProductDto = TestDataUtilities.createTestProductDtoA();
        testProductDto.setProductName("UPDATED");

        String jsonTestProductDto = objectMapper.writeValueAsString(testProductDto);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/product/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestProductDto)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateProductReturnsUpdatedProduct() throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntity);

        ProductDto testProductDto = TestDataUtilities.createTestProductDtoA();
        testProductDto.setProductName("UPDATED");

        String jsonTestProductDto = objectMapper.writeValueAsString(testProductDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/product/" + testProductDto.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestProductDto)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productId").value(1L)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productName").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productDescription").value("Plank with keys")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productPrice").value(BigDecimal.valueOf(11.11))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.productCurrency").value("EUR")
        );
    }

    @Test
    public void testThatPartialUpdateProductReturnsHttpStatus404NotFound() throws Exception{
        ProductDto testProductDto = TestDataUtilities.createTestProductDtoA();
        testProductDto.setProductName("UPDATED");

        String jsonTestProductDto = objectMapper.writeValueAsString(testProductDto);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/product/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTestProductDto)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteProductReturnsHttpStatus204NoContent() throws Exception {
        ProductEntity testProductEntity = TestDataUtilities.createTestProductEntityA();
        productService.save(testProductEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/product/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
