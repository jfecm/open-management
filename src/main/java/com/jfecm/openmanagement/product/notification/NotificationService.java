package com.jfecm.openmanagement.product.notification;

import com.jfecm.openmanagement.product.dtos.ProductResponse;

import java.util.List;

public interface NotificationService {
    void sendLowStockNotification(List<ProductResponse> product);
}
