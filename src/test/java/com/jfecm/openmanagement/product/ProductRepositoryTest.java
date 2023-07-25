package com.jfecm.openmanagement.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {
    int lowStockThreshold;
    Specification<Product> specification;
    Pageable pageable;
    List<Product> lowStockProducts;
    List<Product> productList;

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
        productList = ProductTestDataBuilder.createProducts();
        productRepository.saveAll(productList);
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
        assertEquals(6, productsPage.getContent().size());
    }

    @DisplayName("Test findAll by inStock")
    @Test
    void testFindAllByInStock() {
        specification = specification.and((root, query, builder) -> builder.equal(root.get("inStock"), true));
        Page<Product> productsPage = productRepository.findAll(specification, pageable);
        assertEquals(10, productsPage.getContent().size());
    }

    @DisplayName("Test findAll by available quantity less than 10")
    @Test
    void testFindAllByAvailableQuantity() {
        specification = specification.and((root, query, builder) -> builder.lessThan(root.get("availableQuantity"), 10));

        Page<Product> productsPage = productRepository.findAll(specification, pageable);

        assertAll("Products with available quantity less than 10",
                () -> assertEquals(4, productsPage.getContent().size(), "Number of products is incorrect"),
                () -> assertTrue(productsPage.getContent().stream().allMatch(p -> p.getAvailableQuantity() < 10),
                        "Not all products have available quantity less than 10")
        );
    }

    @Test
    void testFindByAvailableQuantityLessThan() {
        lowStockProducts = productRepository.findByAvailableQuantityLessThan(lowStockThreshold);
        assertEquals(4, lowStockProducts.size());
    }

}