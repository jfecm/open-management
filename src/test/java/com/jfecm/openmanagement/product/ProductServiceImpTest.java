package com.jfecm.openmanagement.product;

import com.jfecm.openmanagement.exception.NullProductDataException;
import com.jfecm.openmanagement.exception.ResourceNotFoundException;
import com.jfecm.openmanagement.notification.Constants;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpTest {

    private ProductConvert productConvert;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImp productService;
    private Product productToTest;
    private Product productToSave;
    private List<Product> productList;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        saveProducts();
        productConvert = new ProductConvert(new ModelMapper());
        productService = new ProductServiceImp(productRepository, productConvert);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    private void saveProducts() {
        productList = ProductTestDataBuilder.createProducts();
        productRepository.saveAll(productList);
        productToSave = ProductTestDataBuilder.createProductToSave();
        productToTest = productList.get(0);
    }

    @Test
    @DisplayName("Test ProductService create() method - Success")
    void testCreateSuccess() {
        // GIVEN
        given(productRepository.save(productToSave)).willReturn(productToSave);

        // WHEN
        productRequest = productConvert.ToRequest(productToSave);
        ProductResponse productResponse = productService.create(productRequest);

        // THEN
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(productToSave.getName());
        assertThat(productResponse.getPrice()).isEqualTo(productToSave.getPrice());

        // Verify that the repository save method was called just 1 time.
        verify(productRepository, times(1)).save(productToSave);
    }

    @Test
    @DisplayName("Test ProductService create() method - Null Product")
    void testCreateWithNullProduct() {
        // GIVEN
        productRequest = null;

        // WHEN & THEN
        assertThrows(NullProductDataException.class, () -> productService.create(productRequest));

        // Verify that the repository save method was never called.
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test ProductService get() method - Success")
    void testGetSuccess() {
        // GIVEN
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.ofNullable(productToTest));

        // WHEN
        ProductResponse productResponse = productService.get(productId);

        // THEN
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(productToTest.getName());
        assertThat(productResponse.getPrice()).isEqualTo(productToTest.getPrice());
    }

    @Test
    @DisplayName("Test ProductService get() method - Failure")
    void testGetFailure() {
        // GIVEN
        Long nonExistentProductId = 100L;

        when(productRepository.findById(nonExistentProductId)).thenThrow(new ResourceNotFoundException("Product not found."));

        // WHEN & THEN
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.get(nonExistentProductId));
    }

    @Test
    @DisplayName("Test ProductService getAll() method - Pagination & Filtering")
    void testGetAll() {
        // GIVEN
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        // Simulates that the repository returns the sample product page when findAll() is called with the expected specification and pagination.
        when(productRepository.findAll((Specification<Product>) any(), eq(pageable))).thenReturn(productPage);

        // WHEN
        Page<ProductResponse> productResponsePage = productService.getAll(new ProductFilter(), pageable);

        // THEN
        Assertions.assertEquals(productList.size(), productResponsePage.getTotalElements());

        // Verifies that each product in the response list has the expected data
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            ProductResponse productResponse = productResponsePage.getContent().get(i);
            Assertions.assertEquals(product.getName(), productResponse.getName());
        }
    }

    @Test
    @DisplayName("Test ProductService getProductsInLowStock() method")
    void testGetProductsInLowStock() {
        // GIVEN
        int lowStockThreshold = Constants.LOW_STOCK_THRESHOLD;
        when(productRepository.findByAvailableQuantityLessThan(lowStockThreshold)).thenReturn(productList);

        // WHEN
        List<ProductResponse> productResponses = productService.getProductsInLowStock();

        // THEN
        Assertions.assertEquals(11, productResponses.size());
        verify(productRepository, times(1)).findByAvailableQuantityLessThan(lowStockThreshold);
    }

    @Test
    @DisplayName("Test ProductService update() method")
    void testUpdate() {
        // GIVEN
        Long productId = 1L;
        ProductRequest updatedProductRequest = new ProductRequest();
        updatedProductRequest.setName("Updated Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(productToTest));
        when(productRepository.save(any())).thenReturn(productToTest);

        // WHEN
        ProductResponse productResponse = productService.update(productId, updatedProductRequest);

        // THEN
        Assertions.assertEquals(productToTest.getName(), updatedProductRequest.getName());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(productToTest);
    }

    @Test
    @DisplayName("Test ProductService updateStock() method")
    void testUpdateStock() {
        // GIVEN
        Long productId = 1L;
        Boolean newStockStatus = false;

        when(productRepository.findById(productId)).thenReturn(Optional.of(productToTest));
        when(productRepository.save(any())).thenReturn(productToTest);

        // WHEN
        ProductResponse productResponse = productService.updateStock(productId, newStockStatus);

        // THEN
        Assertions.assertEquals(newStockStatus, productResponse.isInStock());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(productToTest);
    }

    @Test
    @DisplayName("Test ProductService updateSale() method")
    void testUpdateSale() {
        // GIVEN
        Long productId = 1L;
        Boolean newSaleStatus = true;

        when(productRepository.findById(productId)).thenReturn(Optional.of(productToTest));
        when(productRepository.save(any())).thenReturn(productToTest);

        // WHEN
        ProductResponse productResponse = productService.updateSale(productId, newSaleStatus);

        // THEN
        Assertions.assertEquals(newSaleStatus, productResponse.isOnSale());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(productToTest);
    }

    @Test
    @DisplayName("Test ProductService updateStockAndSale() method")
    void testUpdateStockAndSale() {
        // GIVEN
        Long productId = 1L;
        Boolean newStockStatus = false;
        Boolean newSaleStatus = true;

        when(productRepository.findById(productId)).thenReturn(Optional.of(productToTest));
        when(productRepository.save(any())).thenReturn(productToTest);

        // WHEN
        ProductResponse productResponse = productService.updateStockAndSale(productId, newStockStatus, newSaleStatus);

        // THEN
        Assertions.assertEquals(newStockStatus, productResponse.isInStock());
        Assertions.assertEquals(newSaleStatus, productResponse.isOnSale());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(productToTest);
    }

    @Test
    @DisplayName("Test ProductService delete() method")
    void testDelete() {
        // GIVEN
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(productToTest));

        // WHEN
        productService.delete(productId);

        // THEN
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(productToTest);
    }

    @Test
    @DisplayName("Test ProductService findOneOrThrow() - Not Found")
    void testFindOneOrThrow() {
        // GIVEN
        Long nonExistentProductId = 100L;
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> productService.findOneOrThrow(nonExistentProductId));
    }
}