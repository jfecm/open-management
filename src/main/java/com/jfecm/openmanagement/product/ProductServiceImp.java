package com.jfecm.openmanagement.product;

import com.jfecm.openmanagement.exception.ResourceNotFoundException;
import com.jfecm.openmanagement.notification.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConvert productConvert;

    @Override
    public ProductResponse create(ProductRequest product) {
        Product savedProduct = productRepository.save(productConvert.toEntity(product));

        return productConvert.ToResponse(savedProduct);
    }

    @Override
    public ProductResponse get(Long id) {
        return productConvert.ToResponse(findOneOrThrow(id));
    }

    @Override
    public Page<ProductResponse> getAll(ProductFilter filter, Pageable pageable) {
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

        // Get the product page using the spec and the pageable
        Page<Product> products = productRepository.findAll(specification, pageable);

        // Convert each Product entity to a ProductResponse entity
        return products.map(productConvert::ToResponse);
    }

    @Override
    public List<ProductResponse> getProductsInLowStock() {
        return productRepository.findByAvailableQuantityLessThan(Constants.LOW_STOCK_THRESHOLD)
                .stream()
                .map(productConvert::ToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse update(Long id, ProductRequest product) {
        Product existingProduct = findOneOrThrow(id);

        productConvert.toUpdate(id, product, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStock(Long id, Boolean inStock) {
        Product existingProduct = findOneOrThrow(id);
        existingProduct.setInStock(inStock);
        Product updatedProduct = productRepository.save(existingProduct);
        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateSale(Long id, Boolean onSale) {
        Product existingProduct = findOneOrThrow(id);
        existingProduct.setOnSale(onSale);
        Product updatedProduct = productRepository.save(existingProduct);
        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateStockAndSale(Long id, Boolean inStock, Boolean onSale) {
        Product existingProduct = findOneOrThrow(id);
        existingProduct.setInStock(inStock);
        existingProduct.setOnSale(onSale);
        Product updatedProduct = productRepository.save(existingProduct);
        return productConvert.ToResponse(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        productRepository.delete(findOneOrThrow(id));
    }

    @Override
    public Product findOneOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No product found with id " + id));
    }
}
