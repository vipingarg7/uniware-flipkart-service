package com.uniware.integrations.client.dto.uniware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchOrderResponse {

    private List<SaleOrder> orders;
    private String nextToken;
    private boolean hasMoreOrders = false;
    private Map<String,Object> metadata;
    private int totalPages;

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

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public boolean isHasMoreOrders() {
        return hasMoreOrders;
    }

    public void setHasMoreOrders(boolean hasMoreOrders) {
        this.hasMoreOrders = hasMoreOrders;
    }

    public Map<String, Object> getMetadata() {
        if ( this.metadata == null )
            this.metadata = new HashMap<>();
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
