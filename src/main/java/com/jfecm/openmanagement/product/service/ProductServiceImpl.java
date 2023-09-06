package com.jfecm.openmanagement.product.service;

import com.jfecm.openmanagement.exception.ErrorMessageConstants;
import com.jfecm.openmanagement.exception.NullProductDataException;
import com.jfecm.openmanagement.exception.ProductNameAlreadyExistsException;
import com.jfecm.openmanagement.exception.ResourceNotFoundException;
import com.jfecm.openmanagement.product.dtos.ProductFilter;
import com.jfecm.openmanagement.product.dtos.ProductRequest;
import com.jfecm.openmanagement.product.dtos.ProductResponse;
import com.jfecm.openmanagement.product.model.Product;
import com.jfecm.openmanagement.product.notification.Constants;
import com.jfecm.openmanagement.product.repository.ProductRepository;
import com.jfecm.openmanagement.product.util.ProductModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductModelMapper productModelMapper;

    @Override
    public ProductResponse create(ProductRequest product) {
        log.info("Product details before create : {}", product);

        if (product == null) {
            log.warn("Attempted to create a product with null product data.");
            throw new NullProductDataException(ErrorMessageConstants.NULL_PRODUCT_DATA);
        }

        if (productRepository.findByName(product.getName())) {
            log.warn("Product with name {} already exists. Cannot create a new product with the same name.", product.getName());
            throw new ProductNameAlreadyExistsException(ErrorMessageConstants.PRODUCT_NAME_ALREADY_EXISTS);
        }

        Product savedProduct = productRepository.save(productModelMapper.toEntity(product));
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productModelMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse get(Long id) {
        log.debug("Getting product with ID: {}", id);
        Product product = findOneOrThrow(id);
        log.trace("Product details: {}", product);
        return productModelMapper.toResponse(product);
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

        if (filter.getMinPrice() != null && filter.getMinPrice() > 0) {
            specification = specification.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null && filter.getMaxPrice() > 0) {
            specification = specification.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        if (filter.getOnSale() != null) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get("onSale"), filter.getOnSale()));
        }

        if (filter.getInStock() != null) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get("inStock"), filter.getInStock()));
        }

        if (filter.getAvailableQuantity() != null && filter.getAvailableQuantity() >= 0) {
            specification = specification.and((root, query, builder) -> builder.lessThan(root.get("availableQuantity"), filter.getAvailableQuantity()));
        }

        log.info("Total Size before filtering: {}", productRepository.findAll().size());
        log.debug("Executing the query to retrieve products based on the provided filter and pageable");
        // Get the product page using the spec and the pageable
        Page<Product> products = productRepository.findAll(specification, pageable);

        log.info("Total Size after filtering: {}", products.getTotalElements());
        log.debug("Converting each Product entity to a ProductResponse entity");
        // Convert each Product entity to a ProductResponse entity
        return products.map(productModelMapper::toResponse);
    }

    @Override
    public List<ProductResponse> getProductsInLowStock() {
        log.trace("Getting products with low stock");

        List<Product> products = productRepository.findByAvailableQuantityLessThan(Constants.LOW_STOCK_THRESHOLD);

        List<ProductResponse> productResponses = products.stream().map(productModelMapper::toResponse).toList();

        log.debug("ProductResponses with low stock: {}", productResponses);

        return productResponses;
    }

    @Override
    public ProductResponse update(Long id, ProductRequest product) {
        log.info("Updating product with ID: {} - Product details before update: {}", id, product);
        Product existingProduct = findOneOrThrow(id);

        productModelMapper.toUpdate(product, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product details after updated successfully {}", updatedProduct);

        return productModelMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStock(Long id, Boolean inStock) {
        log.info("Updating stock for product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);
        existingProduct.setInStock(inStock);

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product stock updated successfully with ID: {}", id);
        log.info("Product details after stock update: {}", updatedProduct);

        return productModelMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateSale(Long id, Boolean onSale) {
        log.info("Updating sale status for product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);
        existingProduct.setOnSale(onSale);

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product details after sale status update: {}", updatedProduct);

        return productModelMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStockAndSale(Long id, Boolean inStock, Boolean onSale) {
        log.info("Updating stock and sale status for product with ID: {}", id);

        Product existingProduct = findOneOrThrow(id);
        existingProduct.setInStock(inStock);
        existingProduct.setOnSale(onSale);

        Product updatedProduct = productRepository.save(existingProduct);

        log.info("Product details after stock and sale status update: {}", updatedProduct);

        return productModelMapper.toResponse(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        productRepository.delete(findOneOrThrow(id));
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public Product findOneOrThrow(Long id) {
        log.info("Finding product with ID: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Product with ID {} not found", id);
            return new ResourceNotFoundException(ErrorMessageConstants.PRODUCT_NOT_FOUND + id);
        });
        log.info("Product details: {}", product);
        return product;
    }
}
