package com.jfecm.openmanagement.notification;

import com.jfecm.openmanagement.product.ProductResponse;

import java.util.List;

public interface NotificationService {
    void sendLowStockNotification(List<ProductResponse> product);
}
