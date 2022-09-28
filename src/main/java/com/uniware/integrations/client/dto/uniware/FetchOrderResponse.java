package com.uniware.integrations.client.dto.uniware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FetchOrderResponse {

    List<SaleOrder> orders;

    Map<String,Object> metadata;

    public FetchOrderResponse addOrders(List<SaleOrder> orders) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        this.orders.addAll(orders);
        return this;
    }
    public List<SaleOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<SaleOrder> orders) {
        this.orders = orders;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
