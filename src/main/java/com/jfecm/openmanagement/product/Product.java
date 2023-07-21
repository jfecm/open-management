package com.jfecm.openmanagement.product;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
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
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
}
