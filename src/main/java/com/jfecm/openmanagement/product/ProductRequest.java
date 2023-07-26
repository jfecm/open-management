package com.jfecm.openmanagement.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(max = 80, message = "Name must be at most 80 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be at most 120 characters")
    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private Double price;
    @Min(value = 0, message = "Available quantity must be greater than or equal to 0")
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
