package com.jfecm.openmanagement.product;

import com.jfecm.openmanagement.product.model.Product;
import com.jfecm.openmanagement.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    private final List<Product> productList = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Product productOne = Product.builder()
                .name("Digital Camera")
                .description("High-resolution mirror less digital camera.")
                .price(1299.0)
                .availableQuantity(6)
                .onSale(Boolean.FALSE)
                .brand("Sony")
                .model("Alpha A7 III")
                .color("Black")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Digital Camera")
                .imageURL("digital_camera_image.jpg")
                .inStock(Boolean.TRUE)
                .releaseDate(new Date())
                .build();
        productList.add(productOne);

        Product productTwo = Product.builder()
                .name("Fitness Tracker")
                .description("Wearable fitness tracker with heart rate monitor.")
                .price(69.0)
                .availableQuantity(18)
                .onSale(Boolean.TRUE)
                .brand("Garmin")
                .model("Vivosmart 4")
                .color("Rose Gold")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Fitness Tracker")
                .imageURL("fitness_tracker_image.jpg")
                .inStock(Boolean.TRUE)
                .releaseDate(new Date())
                .build();
        productList.add(productTwo);

        productRepository.saveAll(productList);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Test ProductRepository findByAvailableQuantityLessThan() method - Success")
    void testFindAllSuccess() {
        // given
        Specification<Product> specification = Specification.where((root, query, builder) -> builder.equal(root.get("onSale"), Boolean.TRUE));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Product> productsPage = productRepository.findAll(specification, pageable);

        // then
        assertThat(productsPage.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test ProductRepository findByAvailableQuantityLessThan() method - Failure")
    void testFindAllFailure() {
        // given
        Specification<Product> specification = Specification.where((root, query, builder) -> builder.equal(root.get("onSale"), Boolean.TRUE));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Product> productsPage = productRepository.findAll(specification, pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotEqualTo(2);
    }


    @Test
    @DisplayName("Test ProductRepository findByAvailableQuantityLessThan() method - Success")
    void testFindByAvailableQuantityLessThanSuccess() {
        // given
        int lowStockThreshold = 20;
        // when
        List<Product> resultList = productRepository.findByAvailableQuantityLessThan(lowStockThreshold);
        // then
        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test ProductRepository findByAvailableQuantityLessThan() method - Failure")
    void testFindByAvailableQuantityLessThanFailure() {
        // given
        int lowStockThreshold = 10;
        // when
        List<Product> resultList = productRepository.findByAvailableQuantityLessThan(lowStockThreshold);
        // then
        assertThat(resultList).isNotNull();
        assertThat(resultList).isNotEmpty();
        assertThat(resultList.size()).isNotEqualTo(2);
        assertThat(resultList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test ProductRepository findByName() method - Success")
    void testFindByNameSuccess() {
        // given
        String name = "Digital Camera";
        // when
        boolean result = productRepository.findByName(name);
        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("Test ProductRepository findByName() method - Failure")
    void testFindByNameFailure() {
        // given
        String name = "Camera mobile";
        // when
        Boolean result = productRepository.findByName(name);
        // then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("Test ProductRepository save() method - Success")
    void testSaveSuccess() {
        // given
        Product product = ProductTestDataBuilder.getProductEntity();

        // when
        Product productSaved = productRepository.save(product);

        // then
        assertThat(productSaved.getName()).isEqualTo(product.getName());
    }

}