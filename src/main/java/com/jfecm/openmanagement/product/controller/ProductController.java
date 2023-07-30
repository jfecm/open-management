package com.jfecm.openmanagement.product.controller;

import com.jfecm.openmanagement.product.dtos.ProductFilter;
import com.jfecm.openmanagement.product.dtos.ProductRequest;
import com.jfecm.openmanagement.product.dtos.ProductResponse;
import com.jfecm.openmanagement.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/product-inventory")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest product) {
        log.info("Creating a new product: {}", product);
        // Create a new product and return the response with code 201 (CREATED)
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        log.info("Getting product by id: {}", id);
        // Get the product by its id and return the response with code 200 (OK)
        return new ResponseEntity<>(productService.get(id), HttpStatus.OK);
    }

    @GetMapping("/products/all-products")
    public ResponseEntity<Page<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @ModelAttribute ProductFilter filter) {
        log.info("Getting all products with page: {}, size: {}, sort: {}, filters: {}", page, size, sort, filter);

        // Create a Pageable object for pagination and ordering
        Pageable pageable = PageRequest.of(page, size, getSortDirection(sort), sort[0]);

        // Get the product page based on the filter criteria
        Page<ProductResponse> productPage = productService.getAll(filter, pageable);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest product) {
        log.info("Updating product with id: {}, data: {}", id, product);
        // Update the product by its id and return the response with code 200 (OK)
        return new ResponseEntity<>(productService.update(id, product), HttpStatus.OK);
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id, @RequestParam Boolean inStock) {
        log.info("Updating stock for product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateStock(id, inStock);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}/sale")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse> updateSale(@PathVariable Long id, @RequestParam Boolean onSale) {
        log.info("Updating onSale for product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateSale(id, onSale);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}/stock-and-sale")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse> updateStockAndSale(@PathVariable Long id, @RequestParam(required = false) Boolean inStock, @RequestParam(required = false) Boolean onSale) {
        log.info("Updating stock and sale for product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateStockAndSale(id, inStock, onSale);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        // Remove the product by its id and return a response with no content with code 204 (NO_CONTENT)
        productService.delete(id);
    }

    private Sort.Direction getSortDirection(String[] sort) {
        if (sort[1].equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }
}
