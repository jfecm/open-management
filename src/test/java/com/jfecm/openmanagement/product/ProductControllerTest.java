package com.jfecm.openmanagement.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jfecm.openmanagement.product.controller.ProductController;
import com.jfecm.openmanagement.product.dtos.ProductRequest;
import com.jfecm.openmanagement.product.dtos.ProductResponse;
import com.jfecm.openmanagement.product.model.Product;
import com.jfecm.openmanagement.product.service.ProductService;
import com.jfecm.openmanagement.product.util.ProductModelMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    Product productOne;
    Product productTwo;
    ProductRequest productRequest;
    ProductResponse productResponse;
    List<Product> productList;
    private ProductModelMapper productModelMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productModelMapper = new ProductModelMapper(new ModelMapper());
        productResponse = new ProductResponse();
        productRequest = ProductRequest
                .builder()
                .id(1L)
                .name("Smartphone")
                .description("Flagship smartphone with dual-camera setup.")
                .price(899.0)
                .availableQuantity(12)
                .onSale(true)
                .brand("Samsung")
                .model("Galaxy S21+")
                .color("Phantom Black")
                .capacity("256GB")
                .operatingSystem("Android 12")
                .type("Smartphone")
                .imageURL("smartphone_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build();

        productOne = Product.builder().name("Smartphone").description("Flagship smartphone with dual-camera setup.").price(899.0).availableQuantity(12).onSale(true).brand("Samsung").model("Galaxy S21+").color("Phantom Black").capacity("256GB").operatingSystem("Android 12").type("Smartphone").imageURL("smartphone_image.jpg").inStock(true).releaseDate(new Date()).build();

        productTwo = Product.builder().name("Laptop").description("High-performance laptop for productivity and gaming.").price(1299.99).availableQuantity(8).onSale(false).brand("Dell").model("XPS 15").color("Silver").capacity("512GB SSD").operatingSystem("Windows 11").type("Laptop").imageURL("laptop_image.jpg").inStock(true).releaseDate(new Date()).build();

        productList = new ArrayList<>();
        productList.add(productOne);
        productList.add(productTwo);
    }

    @AfterEach
    void tearDown() {
        productList = null;
    }

    @Test
    @DisplayName("Test ProductController create() method - Success")
    void testCreate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(productRequest);

        when(productService.create(productRequest)).thenReturn(productResponse);

        this.mockMvc
                .perform(
                        post("/api/v1/product-inventory")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test ProductController get() method - Success")
    void testGet() throws Exception {
        Long productId = 1L;
        when(productService.get(productId)).thenReturn(productModelMapper.ToResponse(productOne));

        this.mockMvc
                .perform(
                        get("/api/v1/product-inventory/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(899.0))
                .andExpect(jsonPath("$.brand").value("Samsung"));
    }

    @Test
    @DisplayName("Test ProductController getAll() method - Success")
    void testGetAll() throws Exception {
        when(productService.getAll(null, null)).thenReturn(null);
        this.mockMvc
                .perform(get("/api/v1/product-inventory"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test ProductController update() method - Success")
    void testUpdate() throws Exception {
        Long productId = 1L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(productRequest);

        when(productService.update(1L, productRequest)).thenReturn(productResponse);

        this.mockMvc
                .perform(
                        put("/api/v1/product-inventory/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test ProductController delete() method - Success")
    void testDelete() throws Exception {
        Long productId = 1L;

        this.mockMvc
                .perform(
                        delete("/api/v1/product-inventory/{id}", productId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(productId);
    }
}