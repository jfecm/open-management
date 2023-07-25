package com.jfecm.openmanagement.notification;

import com.jfecm.openmanagement.product.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService {

    @Override
    public void sendLowStockNotification(List<ProductResponse> product) {
        for (ProductResponse p : product) {
            log.info("Sending notification for low stock product: {}", p.getName());
        }

        // TODO - SEND EMAIL NOTIFICATION
    }

}
