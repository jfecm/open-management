package com.jfecm.openmanagement.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductTestDataBuilder {

    public static List<Product> createProducts() {

        List<Product> products = new ArrayList<>();

        products.add(Product.builder()
                .name("Gaming Laptop")
                .description("15.6-inch gaming laptop with powerful hardware for gaming enthusiasts.")
                .price(1799.99)
                .availableQuantity(10)
                .onSale(false)
                .brand("ASUS")
                .model("ROG Zephyrus G15")
                .color("Gray")
                .capacity("512GB SSD")
                .operatingSystem("Windows 10 Home")
                .type("Gaming Laptop")
                .imageURL("gaming_laptop_image.jpg")
                .inStock(true)
                .releaseDate(parseDate("2022-03-15"))
                .build());

        products.add(Product.builder()
                .name("Ultra HD Smart TV")
                .description("65-inch 4K Ultra HD Smart TV with built-in streaming apps.")
                .price(1299.0)
                .availableQuantity(5)
                .onSale(true)
                .brand("Samsung")
                .model("OLED65C1PUB")
                .color("Black")
                .capacity("65 inches")
                .operatingSystem("webOS")
                .type("Smart TV")
                .imageURL("smart_tv_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Gaming Laptop")
                .description("High-performance gaming laptop with RGB lighting.")
                .price(1599.99)
                .availableQuantity(8)
                .onSale(false)
                .brand("Acer")
                .model("Predator Helios 300")
                .color("Black")
                .capacity("1TB SSD")
                .operatingSystem("Windows 10")
                .type("Laptop")
                .imageURL("gaming_laptop_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Wireless Earbuds")
                .description("True wireless earbuds with noise-cancellation.")
                .price(89.99)
                .availableQuantity(20)
                .onSale(true)
                .brand("Sony")
                .model("WF-1000XM4")
                .color("Silver")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Earbuds")
                .imageURL("wireless_earbuds_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Smartphone")
                .description("Flagship smartphone with dual-camera setup.")
                .price(899.0)
                .availableQuantity(12)
                .onSale(true)
                .brand("Samsung")
                .model("Galaxy S21+")
                .color("Phantom Black")
                .capacity("256GB")
                .operatingSystem("Android 12")
                .type("Smartphone")
                .imageURL("smartphone_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Wireless Headphones")
                .description("Over-ear wireless headphones with active noise-cancellation.")
                .price(249.99)
                .availableQuantity(15)
                .onSale(false)
                .brand("Bose")
                .model("QuietComfort 35 II")
                .color("Silver")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Headphones")
                .imageURL("wireless_headphones_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Smart Watch")
                .description("Fitness and activity tracking smart watch.")
                .price(149.0)
                .availableQuantity(7)
                .onSale(true)
                .brand("Fitbit")
                .model("Charge 4")
                .color("Black")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Smart Watch")
                .imageURL("smart_watch_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Portable Bluetooth Speaker")
                .description("Compact and portable Bluetooth speaker for outdoor use.")
                .price(79.99)
                .availableQuantity(25)
                .onSale(true)
                .brand("JBL")
                .model("Flip 5")
                .color("Blue")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Speaker")
                .imageURL("bluetooth_speaker_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("External SSD Drive")
                .description("High-speed external SSD drive for data storage.")
                .price(179.0)
                .availableQuantity(10)
                .onSale(false)
                .brand("Samsung")
                .model("T5")
                .color("Gray")
                .capacity("1TB")
                .operatingSystem("Any")
                .type("External Drive")
                .imageURL("external_ssd_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Fitness Tracker")
                .description("Wearable fitness tracker with heart rate monitor.")
                .price(69.0)
                .availableQuantity(18)
                .onSale(true)
                .brand("Garmin")
                .model("Vivosmart 4")
                .color("Rose Gold")
                .capacity("N/A")
                .operatingSystem("Any")
                .type("Fitness Tracker")
                .imageURL("fitness_tracker_image.jpg")
                .inStock(true)
                .releaseDate(new Date())
                .build());

        products.add(Product.builder()
                .name("Digital Camera")
                .description("High-resolution mirrorless digital camera.")
                .price(1299.0)
                .availableQuantity(6)
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
                .build());


        return products;
    }

    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Product createProductToSave() {
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
