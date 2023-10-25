package com.jfecm.openmanagement.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String model;
    private String brand;
    private Double minPrice;
    private Double maxPrice;
    private Boolean onSale;
    private Boolean inStock;
    private Boolean inOffer;
    private Integer availableQuantity;
}
