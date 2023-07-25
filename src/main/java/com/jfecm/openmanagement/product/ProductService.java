package com.jfecm.openmanagement.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest product);

    ProductResponse get(Long id);

    Page<ProductResponse> getAll(ProductFilter filter, Pageable pageable);

    List<ProductResponse> getProductsInLowStock();

    ProductResponse update(Long id, ProductRequest product);

    ProductResponse updateStock(Long id, Boolean inStock);

    ProductResponse updateSale(Long id, Boolean onSale);

    ProductResponse updateStockAndSale(Long id, Boolean inStock, Boolean onSale);

    void delete(Long id);

    Product findOneOrThrow(Long id);
}
