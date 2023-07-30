package com.jfecm.openmanagement.product;

import com.jfecm.openmanagement.exception.NullProductDataException;
import com.jfecm.openmanagement.exception.ProductNameAlreadyExistsException;
import com.jfecm.openmanagement.exception.ResourceNotFoundException;
import com.jfecm.openmanagement.product.dtos.ProductRequest;
import com.jfecm.openmanagement.product.repository.ProductRepository;
import com.jfecm.openmanagement.product.service.ProductServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @DisplayName("Should throw NullProductDataException() when product is null")
    @Test
    void shouldThrowNullProductDataExceptionWhenProductIsNull() {
        NullProductDataException nullProductDataException = Assertions
                .assertThrows(NullProductDataException.class,
                        () -> productService.create(null));

        Assertions.assertEquals("Product data cannot be null.",
                nullProductDataException.getMessage());
    }

    @DisplayName("Should throw ProductNameAlreadyExistsException() when product name already exists")
    @Test
    void shouldThrowProductNameAlreadyExistsExceptionWhenProductNameAlreadyExists() {
        ProductRequest productRequest = ProductTestDataBuilder.getProductRequest();
        Mockito.when(productRepository.findByName(anyString()))
                .thenReturn(true);

        ProductNameAlreadyExistsException productNameAlreadyExistsException = Assertions
                .assertThrows(ProductNameAlreadyExistsException.class,
                        () -> productService.create(productRequest));

        Assertions.assertEquals("A product with the same name already exists.",
                productNameAlreadyExistsException.getMessage());

        Mockito.verify(productRepository, times(1)).findByName(anyString());
    }

    @DisplayName("Should throw ResourceNotFoundException() when the product id does not exist")
    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductIdNotExist() {
        Mockito.when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> productService.findOneOrThrow(anyLong()));

        Assertions.assertEquals("No product found with id 0",
                resourceNotFoundException.getMessage());

        Mockito.verify(productRepository, times(1)).findById(anyLong());
    }

}