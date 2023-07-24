package com.jfecm.openmanagement.product;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpTest {

    private ProductConvert productConvert;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImp productService;
    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Ultra HD Smart TV")
                .description("65-inch 4K Ultra HD Smart TV with built-in streaming apps.")
                .price(1299.0)
                .availableQuantity(5)
                .onSale(true)
                .brand("Samsung")
                .model("OLED65C1PUB")
                .color("Black")
                .capacity("65 inches")
                .operatingSystem("webOS")
                .type("Smart TV")
                .imageURL("smart_tv_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build();
        productConvert = new ProductConvert(new ModelMapper());
        productService = new ProductServiceImp(productRepository, productConvert);
    }

    @DisplayName("Test ProductService create() method")
    @Test
    void testCreate() {
        // GIVEN
        given(productRepository.save(product)).willReturn(product);
        // WHEN
        productRequest = productConvert.ToRequest(product);
        ProductResponse productResponse = productService.create(productRequest);
        // THEN
        assertThat(productResponse).isNotNull();
    }

    @Test
    @Disabled
    void testGet() {
    }

    @Test
    @Disabled
    void testGetAll() {
    }

    @Test
    @Disabled
    void testGetProductsInLowStock() {
    }

    @Test
    @Disabled
    void testUpdate() {
    }

    @Test
    @Disabled
    void testUpdateStock() {
    }

    @Test
    @Disabled
    void testUpdateSale() {
    }

    @Test
    @Disabled
    void testUpdateStockAndSale() {
    }

    @Test
    @Disabled
    void testDelete() {
    }

    @Test
    @Disabled
    void testFindOneOrThrow() {
    }
}