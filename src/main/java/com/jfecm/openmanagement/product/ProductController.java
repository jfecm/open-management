package com.jfecm.openmanagement.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/product-inventory")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest product) {
        log.info("Creating a new product: {}", product);
        // Create a new product and return the response with code 201 (CREATED)
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        log.info("Getting product by id: {}", id);
        // Get the product by its id and return the response with code 200 (OK)
        return new ResponseEntity<>(productService.get(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id,asc") String[] sort, @RequestParam(required = false) String model, @RequestParam(required = false) String brand, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) Boolean onSale, @RequestParam(required = false) Boolean inStock) {
        log.info("Getting all products with page: {}, size: {}, sort: {}, model: {}, brand: {}, minPrice: {}, maxPrice: {}, onSale: {}, inStock: {}", page, size, sort, model, brand, minPrice, maxPrice, onSale, inStock);

        // Create a Pageable object for pagination and ordering
        Pageable pageable = PageRequest.of(page, size, getSortDirection(sort), sort[0]);

        // Create a ProductFilter object to hold the filter criteria
        ProductFilter filter = new ProductFilter(model, brand, minPrice, maxPrice, onSale, inStock);

        // Get the product page based on the filter criteria
        Page<ProductResponse> productPage = productService.getAll(filter, pageable);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest product) {
        log.info("Updating product with id: {}, data: {}", id, product);
        // Update the product by its id and return the response with code 200 (OK)
        return new ResponseEntity<>(productService.update(id, product), HttpStatus.OK);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id, @RequestParam Boolean inStock) {
        log.info("Updating stock for product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateStock(id, inStock);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}/sale")
    public ResponseEntity<ProductResponse> updateSale(@PathVariable Long id, @RequestParam Boolean onSale) {
        log.info("Updating onSale for product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateSale(id, onSale);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{id}/stock-and-sale")
    public ResponseEntity<ProductResponse> updateStockAndSale(@PathVariable Long id, @RequestParam(required = false) Boolean inStock, @RequestParam(required = false) Boolean onSale) {
        log.info("Updating stock and sale for product with ID: {}", id);
        ProductResponse updatedProduct = productService.updateStockAndSale(id, inStock, onSale);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
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
