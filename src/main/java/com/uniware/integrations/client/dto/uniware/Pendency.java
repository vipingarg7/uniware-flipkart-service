package com.uniware.integrations.client.dto.uniware;

public class Pendency {

    private String channelProductId;

    private String sellerSkuCode;

    private String productName;

    private int requiredInventory;

    public String getChannelProductId() {
        return channelProductId;
    }

    public void setChannelProductId(String channelProductId) {
        this.channelProductId = channelProductId;
    }

    public String getSellerSkuCode() {
        return sellerSkuCode;
    }

    public void setSellerSkuCode(String sellerSkuCode) {
        this.sellerSkuCode = sellerSkuCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getRequiredInventory() {
        return requiredInventory;
    }

    public void setRequiredInventory(int requiredInventory) {
        this.requiredInventory = requiredInventory;
    }
}
