package com.jfecm.openmanagement.product;

import com.jfecm.openmanagement.exception.NullProductDataException;
import com.jfecm.openmanagement.exception.ResourceNotFoundException;
import com.jfecm.openmanagement.notification.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConvert productConvert;

    @Override
    public ProductResponse create(ProductRequest product) {

        if (product == null) {
            log.warn("Attempted to create a product with null product data.");
            throw new NullProductDataException("Product data cannot be null.");
        }

        log.info("Creating a new product: {}", product.getName());
        log.debug("Product details: {}", product);
        log.trace("Entering create() method");
        try {
            Product savedProduct = productRepository.save(productConvert.toEntity(product));
            log.info("Product created successfully with ID: {}", savedProduct.getId());
            return productConvert.ToResponse(savedProduct);
        } catch (Exception e) {
            log.error("Error while creating the product: {}", e.getMessage());
            throw e;
        } finally {
            log.trace("Exiting create() method");
        }
    }

    @Override
    public ProductResponse get(Long id) {
        log.debug("Getting product with ID: {}", id);

        Product product = findOneOrThrow(id);

        if (log.isDebugEnabled()) {
            log.debug("Product found: {}", product);
        } else {
            log.info("Product found: {}", product.getName());
        }

        log.trace("Product details: {}", product);

        return productConvert.ToResponse(product);
    }

    @Override
    public Page<ProductResponse> getAll(ProductFilter filter, Pageable pageable) {
        log.trace("Building the specification for the query based on the provided filter");
        // Build a spec for the query based on the provided filter
        Specification<Product> specification = Specification.where(null);

        if (filter.getModel() != null && !filter.getModel().isEmpty()) {
            specification = specification.and((root, query, builder) -> builder.like(builder.lower(root.get("model")), "%" + filter.getModel().toLowerCase() + "%"));
        }

        if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
            specification = specification.and((root, query, builder) -> builder.like(builder.lower(root.get("brand")), "%" + filter.getBrand().toLowerCase() + "%"));
        }

        if (filter.getMinPrice() != null) {
            specification = specification.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null) {
            specification = specification.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        if (filter.getOnSale() != null) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get("onSale"), filter.getOnSale()));
        }

        if (filter.getInStock() != null) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get("inStock"), filter.getInStock()));
        }

        if (filter.getAvailableQuantity() >= 0) {
            specification = specification.and((root, query, builder) -> builder.lessThan(root.get("availableQuantity"), filter.getAvailableQuantity()));
        }

        log.debug("Executing the query to retrieve products based on the provided filter and pageable");
        // Get the product page using the spec and the pageable
        Page<Product> products = productRepository.findAll(specification, pageable);

        log.debug("Converting each Product entity to a ProductResponse entity");
        // Convert each Product entity to a ProductResponse entity
        return products.map(productConvert::ToResponse);
    }

    @Override
    public List<ProductResponse> getProductsInLowStock() {
        log.trace("Getting products with low stock");

        List<Product> products = productRepository.findByAvailableQuantityLessThan(Constants.LOW_STOCK_THRESHOLD);

        log.debug("Products with low stock found: {}", products);

        List<ProductResponse> productResponses = products.stream().map(productConvert::ToResponse).collect(Collectors.toList());

        log.debug("ProductResponses with low stock: {}", productResponses);

        return productResponses;
    }

    @Override
    public ProductResponse update(Long id, ProductRequest product) {
        log.debug("Updating product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);

        productConvert.toUpdate(id, product, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product updated successfully with ID: {}", id);
        log.trace("Product details after update: {}", updatedProduct);
        log.debug("Product updated: {}", updatedProduct);

        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStock(Long id, Boolean inStock) {
        log.debug("Updating stock for product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);
        existingProduct.setInStock(inStock);

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product stock updated successfully with ID: {}", id);
        log.trace("Product details after stock update: {}", updatedProduct);

        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateSale(Long id, Boolean onSale) {
        log.debug("Updating sale status for product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);
        existingProduct.setOnSale(onSale);

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product sale status updated successfully with ID: {}", id);
        log.trace("Product details after sale status update: {}", updatedProduct);

        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStockAndSale(Long id, Boolean inStock, Boolean onSale) {
        log.debug("Updating stock and sale status for product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);
        existingProduct.setInStock(inStock);
        existingProduct.setOnSale(onSale);

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product stock and sale status updated successfully with ID: {}", id);
        log.trace("Product details after stock and sale status update: {}", updatedProduct);

        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting product with ID: {}", id);

        productRepository.delete(findOneOrThrow(id));

        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public Product findOneOrThrow(Long id) {
        log.trace("Finding product with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Product with ID {} not found", id);
            return new ResourceNotFoundException("No product found with id " + id);
        });

        log.debug("Product found: {}", product);

        return product;
    }
}
