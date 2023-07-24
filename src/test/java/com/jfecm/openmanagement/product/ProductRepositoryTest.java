package com.jfecm.openmanagement.product;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {
    int lowStockThreshold;
    Specification<Product> specification;
    Pageable pageable;
    List<Product> lowStockProducts;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        saveProducts();
        lowStockThreshold = 10;
        specification = Specification.where(null);
        pageable = PageRequest.of(0, 10);
        lowStockProducts = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        lowStockThreshold = 0;
        specification = null;
        pageable = null;
        lowStockProducts = null;
        productRepository.deleteAll();
    }

    private void saveProducts() {
        Product product_test_01;
        Product product_test_02;
        Product product_test_03;
        Product product_test_04;

        product_test_01 = Product.builder()
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

        product_test_02 = Product.builder()
                .name("Gaming Laptop")
                .description("High-performance gaming laptop with RGB lighting.")
                .price(1599.99)
                .availableQuantity(8)
                .onSale(false)
                .brand("Acer")
                .model("Predator Helios 300")
                .color("Black")
                .capacity("1TB SSD")
                .operatingSystem("Windows 10")
                .type("Laptop")
                .imageURL("gaming_laptop_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build();

        product_test_03 = Product.builder()
                .name("Wireless Earbuds")
                .description("True wireless earbuds with noise-cancellation.")
                .price(89.99)
                .availableQuantity(20)
                .onSale(true)
                .brand("Sony")
                .model("WF-1000XM4")
                .color("Silver")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Earbuds")
                .imageURL("wireless_earbuds_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build();

        product_test_04 = Product.builder()
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

        productRepository.save(product_test_01);
        productRepository.save(product_test_02);
        productRepository.save(product_test_03);
        productRepository.save(product_test_04);
    }

    @DisplayName("Test findAll by model")
    @ParameterizedTest
    @CsvSource({"Galaxy S21+", "MacBook Pro 16-inch", "OnePlus 9 Pro", "Garmin Venu 2"})
    void testFindAllByModel(String model) {
        specification = specification.and((root, query, builder) -> builder.like(builder.lower(root.get("model")), "%" + model.toLowerCase() + "%"));
        Page<Product> productsPage = productRepository.findAll(specification, pageable);
        assertEquals(1, productsPage.getContent().size());
    }

    @DisplayName("Test findAll by brand")
    @ParameterizedTest
    @CsvSource({"Samsung", "Apple", "Sony", "LG"})
    void testFindAllByBrand(String brand) {
        specification = specification.and((root, query, builder) -> builder.like(builder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%"));
        Page<Product> productsPage = productRepository.findAll(specification, pageable);
        assertEquals(2, productsPage.getContent().size());
    }

    @DisplayName("Test findAll by onSale")
    @Test
    void testFindAllByOnSale() {
        specification = specification.and((root, query, builder) -> builder.equal(root.get("onSale"), true));
        Page<Product> productsPage = productRepository.findAll(specification, pageable);
        assertEquals(3, productsPage.getContent().size());
    }

    @DisplayName("Test findAll by inStock")
    @Test
    void testFindAllByInStock() {
        specification = specification.and((root, query, builder) -> builder.equal(root.get("inStock"), true));
        Page<Product> productsPage = productRepository.findAll(specification, pageable);
        assertEquals(4, productsPage.getContent().size());
    }

    @DisplayName("Test findAll by available quantity less than 10")
    @Test
    void testFindAllByAvailableQuantity() {
        specification = specification.and((root, query, builder) -> builder.lessThan(root.get("availableQuantity"), 10));

        Page<Product> productsPage = productRepository.findAll(specification, pageable);

        assertAll("Products with available quantity less than 10",
                () -> assertEquals(2, productsPage.getContent().size(), "Number of products is incorrect"),
                () -> assertTrue(productsPage.getContent().stream().allMatch(p -> p.getAvailableQuantity() < 10),
                        "Not all products have available quantity less than 10")
        );
    }

    @Test
    void testFindByAvailableQuantityLessThan() {
        lowStockProducts = productRepository.findByAvailableQuantityLessThan(lowStockThreshold);
        assertEquals(2, lowStockProducts.size());
    }

}