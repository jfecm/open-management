package com.jfecm.openmanagement.product;

import java.util.Date;

public class ProductTestDataBuilder {
    public static ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Digital Camera v3")
                .description("Low-resolution.")
                .price(299.0)
                .availableQuantity(4)
                .onSale(false)
                .brand("Sony")
                .model("Alpha A7 III")
                .color("Black")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Digital Camera")
                .imageURL("digital_camera_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build();
    }

    public static Product getProductEntity() {
        return Product.builder()
                .name("Digital Camera v3")
                .description("Low-resolution.")
                .price(299.0)
                .availableQuantity(4)
                .onSale(false)
                .brand("Sony")
                .model("Alpha A7 III")
                .color("Black")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Digital Camera")
                .imageURL("digital_camera_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build();
    }
}
