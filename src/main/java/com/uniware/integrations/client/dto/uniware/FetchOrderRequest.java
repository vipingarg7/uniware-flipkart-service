package com.uniware.integrations.client.dto.uniware;

import java.util.List;
import java.util.Map;

public class FetchOrderRequest {

    private int           orderWindow;
    private int           pageNumber;
    private String         nextToken;
    private List<String>   statuses;
    private Map<String,Object> metadata;

    public int getOrderWindow() {
        return orderWindow;
    }

    public void setOrderWindow(int orderWindow) {
        this.orderWindow = orderWindow;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
