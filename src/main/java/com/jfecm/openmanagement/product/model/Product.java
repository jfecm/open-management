package com.jfecm.openmanagement.product.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name is required")
    @Size(max = 80, message = "Name must be at most 80 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be at most 120 characters")
    private String description;
    @Column(nullable = false)
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
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
}
