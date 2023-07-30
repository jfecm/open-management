package com.jfecm.openmanagement.product.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer availableQuantity;
    private Boolean onSale;
    private String brand;
    private String model;
    private String color;
    private String capacity;
    private String operatingSystem;
    private String type;
    private String imageURL;
    private Boolean inStock;
    private Date releaseDate;
}
