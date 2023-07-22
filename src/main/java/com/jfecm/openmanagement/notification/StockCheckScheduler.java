package com.jfecm.openmanagement.notification;

import com.jfecm.openmanagement.product.ProductResponse;
import com.jfecm.openmanagement.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class StockCheckScheduler {
    private final ProductService productService;
    private final NotificationService notificationService;

    //@Scheduled(fixedRate = 120000) // Run every 2 min (120000 milliseconds) (for test)
    @Scheduled(cron = "0 30 19 * * ?") // Run at 19:30 each day (by default)
    public void checkLowStock() {
        log.info("Checking low stock products...");
        // TODO : users sets the frequency of receiving notifications.
        List<ProductResponse> lowStockProducts = productService.getProductsInLowStock();
        notificationService.sendLowStockNotification(lowStockProducts);
        log.info("Low stock check completed.");
    }

}
