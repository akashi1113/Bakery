package com.csu.bakery.dto;
import com.csu.bakery.model.LineItem;
import com.csu.bakery.model.Order;

import java.util.List;

public class InsertOrderRequest {
    private Order order;
    private List<LineItem> lineItems;

    // Getters and Setters
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
