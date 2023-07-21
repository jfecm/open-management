package com.jfecm.openmanagement.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int availableQuantity;
    private boolean onSale;
    private String brand;
    private String model;
    private String color;
    private String capacity;
    private String operatingSystem;
    private String type;
    private String imageURL;
    private boolean inStock;
    private Date releaseDate;
}
